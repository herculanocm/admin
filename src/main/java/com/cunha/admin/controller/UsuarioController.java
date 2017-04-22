package com.cunha.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cunha.admin.aux.EmailAux;
import com.cunha.admin.daos.UserDAO;
import com.cunha.admin.models.Usuario;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestMapping("/api/admin/usuarios")
@RestController
@Transactional
public class UsuarioController {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private ObjectMapper mapper;
	
	
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(String name, MultipartFile file){
    	System.out.println("comecou o upload");
        if (!file.isEmpty()) {
        	System.out.println("file não e vazio");
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                System.out.println("upload com sucesso");
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
        	System.out.println("file e vazio");
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
	
	@RequestMapping(value="/recovery",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> resetSenha(@RequestBody EmailAux email){
		ObjectNode nodeResposta = mapper.createObjectNode();
		System.out.println("email :"+email.getEmail());
		
		/*
		try { Thread.sleep(10000000); } catch (InterruptedException ex) {
		    System.out.println ("Puxa, estava dormindo! Você me acordou");
		}
		*/
		
		Usuario usuarioLoad= userDAO.carregaUsuairoEmail(email.getEmail());
		if(usuarioLoad == null){
			System.out.println("Usuario não existe");
			nodeResposta.put("descricao", "Usuario não encontrado com esse email.");
			return new ResponseEntity<Object>(nodeResposta,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}else{
			System.out.println("Usuario existe");
			String novaSenha = "Serra2017";
			usuarioLoad.setPassword(new BCryptPasswordEncoder().encode(novaSenha));
			usuarioLoad = userDAO.atualiza(usuarioLoad);
			
			enviaEmail(usuarioLoad.getEmail(), "herculano.cunha2@gmail.com", "Serra Dourada reset senha sitema de Vendas",
					"Olá seu usuário é " + usuarioLoad.getUsername() + " e sua senha " + novaSenha);
			nodeResposta.put("descricao", "Senha alterada com sucesso e enviada para seu email.");
			return new ResponseEntity<Object>(nodeResposta,  new HttpHeaders(), HttpStatus.OK);
		}
	}
	

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> lista() {
		List<Usuario> listaUsuarios = userDAO.listaTodos();
		ObjectNode nodeResposta = mapper.createObjectNode();
		nodeResposta.put("descricao", "Listados com sucesso!");
		// nodeResposta.put("objeto", userRetorno.toString());
		JsonNode node = mapper.convertValue(listaUsuarios, JsonNode.class);
		nodeResposta.set("lista", node);
		
		return new ResponseEntity<>(nodeResposta, new HttpHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value="/salva", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> save(@RequestBody Usuario usuario) throws IOException {
		System.out.println("Salvando o usuario : " + usuario.toString());
		String senhaOriginal= usuario.getPassword();
		usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
		System.out.println("Salvando o usuario : " + usuario.toString());
		usuario.setLogin(usuario.getLogin().toLowerCase());

		HttpStatus httpStatus = null;
		ObjectNode nodeResposta = mapper.createObjectNode();

		if (userDAO.existeUsuarioOrEmail(usuario.getUsername(), usuario.getEmail()) == true) {
			// responseEntity = new ResponseEntity<Object>("deu errado", new
			// HttpHeaders(), HttpStatus.CONFLICT);
			nodeResposta.put("descricao", "O usuario ou o email ja esta cadastrado, cadastre outro!");
			httpStatus = HttpStatus.CONFLICT;
			return new ResponseEntity<Object>(nodeResposta, new HttpHeaders(), httpStatus);
		} else {
			Usuario userRetorno = userDAO.salva(usuario);
			enviaEmail(userRetorno.getEmail(), "herculano.cunha2@gmail.com", "Serra Dourada Conta no sitema de Vendas",
					"Olá seu usuário é " + userRetorno.getUsername() + " e sua senha " + senhaOriginal);

			
			httpStatus = HttpStatus.OK;
			return new ResponseEntity<Object>(userRetorno, new HttpHeaders(), httpStatus);
		}

		

	}

	
	@RequestMapping(value="/atualiza",method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> atualiza(@RequestBody Usuario usuario) {
		System.out.println("Salvando o usuario : " + usuario.toString());
		

		
		HttpStatus httpStatus = null;
		ObjectNode nodeResposta = mapper.createObjectNode();
		
		Usuario usuarioLoad= userDAO.carregaUsuairoLogin(usuario.getLogin().toLowerCase());
		
		if(usuarioLoad == null){
			nodeResposta.put("descricao", "O usuario nao foi encontrado!");
			httpStatus = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Object>(nodeResposta, new HttpHeaders(), httpStatus);
		}else{
			usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
			httpStatus = HttpStatus.OK;
			usuario = userDAO.atualiza(usuario);
			return new ResponseEntity<Object>(usuario, new HttpHeaders(), httpStatus);
		}

	}
	
	
	@RequestMapping(value="/deleta", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleta(@RequestBody Usuario usuario) {
		HttpStatus httpStatus = null;
		ObjectNode nodeResposta = mapper.createObjectNode();
		
		Usuario usuarioLoad= userDAO.carregaUsuairoLogin(usuario.getLogin().toLowerCase());
		
		if(usuarioLoad == null){
			nodeResposta.put("descricao", "O usuario nao foi encontrado!");
			httpStatus = HttpStatus.NOT_FOUND;
		}else{
			userDAO.deleta(usuarioLoad);
			nodeResposta.put("descricao", "Usuario deletado com sucesso!");
			httpStatus = HttpStatus.OK;
		}

		

		

		return new ResponseEntity<Object>(nodeResposta, new HttpHeaders(), httpStatus);
	}
	
	

	public void enviaEmail(String emailTo, String emailFrom, String subject, String textBody) {
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mail, true);
			helper.setTo(emailTo);
			helper.setFrom(emailFrom);
			helper.setSubject(subject);
			helper.setText(textBody);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		javaMailSender.send(mail);
	}

}

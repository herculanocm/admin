package com.cunha.admin.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cunha.admin.daos.UserDAO;
import com.cunha.admin.models.Usuario;
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

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> save(@RequestBody Usuario usuario) throws IOException {
		System.out.println("Salvando o usuario : " + usuario.toString());
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
		} else {
			Usuario userRetorno = userDAO.salva(usuario);
			enviaEmail(userRetorno.getEmail(), "herculano.cunha2@gmail.com", "Serra Dourada Conta no sitema de Vendas",
					"Olá seu usuário é " + userRetorno.getUsername() + " e sua senha " + userRetorno.getPassword());

			nodeResposta.put("descricao", "Usuario cadastrado com sucesso!");
			// nodeResposta.put("objeto", userRetorno.toString());
			JsonNode node = mapper.convertValue(userRetorno, JsonNode.class);
			nodeResposta.set("objeto", node);
			httpStatus = HttpStatus.OK;
		}

		return new ResponseEntity<Object>(nodeResposta, new HttpHeaders(), httpStatus);

	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> atualiza(@RequestBody Usuario usuario) {
		HttpStatus httpStatus = null;
		ObjectNode nodeResposta = mapper.createObjectNode();

		usuario = userDAO.atualiza(usuario);

		nodeResposta.put("descricao", "Usuario atualizado com sucesso!");
	
		// nodeResposta.put("objeto", userRetorno.toString());
		JsonNode node = mapper.convertValue(usuario, JsonNode.class);
		nodeResposta.set("objeto", node);
		httpStatus = HttpStatus.OK;

		return new ResponseEntity<Object>(nodeResposta, new HttpHeaders(), httpStatus);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE)
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

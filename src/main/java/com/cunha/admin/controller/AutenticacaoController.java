package com.cunha.admin.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cunha.admin.daos.UserDAO;
import com.cunha.admin.models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RequestMapping("/api/admin/autenticacao")
@RestController
@Transactional
public class AutenticacaoController {

	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private ObjectMapper mapper;
	

	@Autowired
	private UserDAO userDAO;
	

	
	@RequestMapping(value="/auth",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> autentica(@RequestBody Usuario usuario){
	System.out.println("Autenticando o usuario : "+usuario.toString());
	
	Usuario usuarioLoad= userDAO.carregaUsuairoLogin(usuario.getLogin().toLowerCase());
	System.out.println("Buscou usuario");
	ObjectNode nodeResposta = mapper.createObjectNode();	
	HttpStatus httpStatus=null;
	
	if(usuarioLoad == null){
		nodeResposta.put("descricao", "O usuario não foi encontrado!");
		httpStatus = HttpStatus.NOT_FOUND;
	}else{
		
		if(usuarioLoad.isCredentialsNonExpired() == false || usuarioLoad.isAccountNonExpired() == false || usuarioLoad.isAccountNonLocked() == false || usuarioLoad.isEnabled() == false){
			nodeResposta.put("descricao", "O usuario está bloqueado! Contate o Administrador.");
			httpStatus = HttpStatus.BAD_REQUEST;
		}else{
			
			if(new BCryptPasswordEncoder().matches(usuario.getPassword(), usuarioLoad.getPassword()) == false){
				System.out.println("Senha errada!");
				
				if(usuarioLoad.getErros() == 4){
					usuarioLoad.setErros(usuarioLoad.getErros() + 1);
					usuarioLoad.setCredentialsNonExpired(false);
					nodeResposta.put("descricao", "O usuário foi bloqueado! Contate o Administrador.");
				}else{
					usuarioLoad.setErros(usuarioLoad.getErros() + 1);
					nodeResposta.put("descricao", "A senha não está correta, cuidado mais "+(5 - usuarioLoad.getErros())+" testes errados seu usuário será bloqueado.");
				}
				
				usuarioLoad = userDAO.atualiza(usuarioLoad);
				httpStatus = HttpStatus.BAD_REQUEST;
			}else{
				usuarioLoad.setErros(0);
				usuarioLoad.setCredentialsNonExpired(true);
				usuarioLoad = userDAO.atualiza(usuarioLoad);
				
				System.out.println("Senha correta!");
				httpStatus = HttpStatus.OK;
				//nodeResposta.put("descricao", "A senha está correta");
				//JsonNode node = mapper.convertValue(usuarioLoad, JsonNode.class);
				//nodeResposta.set("objeto", node);
				return new ResponseEntity<Object>(usuarioLoad,  new HttpHeaders(), httpStatus);
			}
			
		}	
		
	}
	
	System.out.println("TERMINOU");
	return new ResponseEntity<Object>(nodeResposta,  new HttpHeaders(), httpStatus);
	
	}
	
	
	public void enviaEmail(String emailTo, String emailFrom, String subject, String textBody){
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

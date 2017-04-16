package com.cunha.admin.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cunha.admin.daos.RoleDAO;
import com.cunha.admin.models.Role;

@RequestMapping("/api/admin/roles")
@RestController
@Transactional
public class RoleController {

	@Autowired
	private RoleDAO roleDAO;
	
	@RequestMapping(method = RequestMethod.GET,value="/lista")
	@ResponseBody
	public ResponseEntity<Object> lista(){
		List<Role> lista=roleDAO.lista();
		return new ResponseEntity<>(lista, new HttpHeaders(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/salva/{role}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> save(@PathVariable(value="role") String role ){
	System.out.println("Salvando a Role : "+role);
	
	Role roleEntity=new Role(role.toUpperCase());
	Role roleRetorno=roleDAO.salva(roleEntity);

	
	
	return new ResponseEntity<>(roleRetorno, new HttpHeaders(), HttpStatus.CREATED);
	}
	
}

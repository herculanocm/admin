package com.cunha.admin.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cunha.admin.daos.GrupoClienteDAO;
import com.cunha.admin.models.GrupoCliente;

@RequestMapping("/api/admin/grupocliente")
@RestController
@Transactional
public class GrupoClienteController {

	@Autowired
	private GrupoClienteDAO grupoClienteDAO;
	
	@RequestMapping(method = RequestMethod.GET,value="/lista")
	@ResponseBody
	public List<GrupoCliente> lista(){
		return grupoClienteDAO.listaTodosGruposClientes();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/salva", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public GrupoCliente salva(@RequestBody GrupoCliente grupoCliente){
		System.out.println("Salvando o grupoCliente : "+grupoCliente.toString());
		return grupoClienteDAO.salvaGrupo(grupoCliente);
	}
	
}

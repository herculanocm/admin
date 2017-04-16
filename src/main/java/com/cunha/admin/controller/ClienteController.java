package com.cunha.admin.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cunha.admin.daos.ClienteDAO;
import com.cunha.admin.models.Cliente;

@RequestMapping("/api/admin/clientes")
@RestController
@Transactional
public class ClienteController {
	
	@Autowired
	private ClienteDAO clienteDAO;
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Cliente save(@Validated @RequestBody Cliente cliente){
	System.out.println("Salvando o cliente : "+cliente.toString());
	
	return clienteDAO.salva(cliente);
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/lista")
	@ResponseBody
	public List<Cliente> list(){
		List<Cliente> lista=clienteDAO.listaTodos();
		System.out.println("Listando clientes: "+ lista.size());
		return  lista;
		}

}

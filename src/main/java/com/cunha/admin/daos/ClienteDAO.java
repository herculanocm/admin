package com.cunha.admin.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.cunha.admin.models.Cliente;

@Repository
public class ClienteDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	public Cliente salva(Cliente cliente){
		em.persist(cliente);
		//em.flush();
		return cliente;
	}
	
	public List<Cliente> listaTodos(){
		String sqlpl="select c from Cliente c";
		List<Cliente> clientes=em.createQuery(sqlpl, Cliente.class).getResultList();
		return clientes;
		
		
	}
			


}

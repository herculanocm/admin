package com.cunha.admin.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.cunha.admin.models.GrupoCliente;

@Repository
public class GrupoClienteDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<GrupoCliente> listaTodosGruposClientes(){
		String jpql = "select g from GrupoCliente g";
		List<GrupoCliente> listaTodosGruposClientes=em.createQuery(jpql).getResultList();
		return listaTodosGruposClientes;
	}
	
	public GrupoCliente salvaGrupo(GrupoCliente grupoCliente){
		em.persist(grupoCliente);
		em.flush();
		return grupoCliente;
	}
	
}

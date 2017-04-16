package com.cunha.admin.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.cunha.admin.models.Role;

@Repository
public class RoleDAO {

	
	@PersistenceContext
	private EntityManager em;
	
	public List<Role> lista(){
		String jpql = "select r from Role r";
		List<Role> roles = em.createQuery(jpql,Role.class).getResultList();
		return roles;
	}
	
	public Role salva(Role role){
		em.persist(role);
		em.flush();
		return role;
	}
}

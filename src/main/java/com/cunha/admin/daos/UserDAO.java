package com.cunha.admin.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.springframework.stereotype.Repository;

import com.cunha.admin.models.Usuario;

@Repository
public class UserDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<Usuario> listaTodos(){
		String jpql = "select u from Usuario u";
		List<Usuario> users = em.createQuery(jpql,Usuario.class).getResultList();
		return users;
	}
	
	
	public Usuario carregaUsuairoLogin(String username) {
		String jpql = "select u from Usuario u where u.login = :login";
		List<Usuario> users = em.createQuery(jpql,Usuario.class).setParameter("login", username).getResultList();
		if(users == null || users.size()==0){
			return null;
		}else{
			System.out.println("Usuario buscado DAO : "+users.get(0).toString());
			return users.get(0);
		}
		
	}
	
	public boolean existeUsuario(String username) {
		String jpql = "select u from Usuario u where u.login = :login";
		List<Usuario> users = em.createQuery(jpql,Usuario.class).setParameter("login", username).getResultList();
		
		if(users.size() > 0){
			return true;
		}
		
		return false;
	}
	
	public Usuario carregaUsuairoEmail(String email) {
		String jpql = "select u from Usuario u where u.email = :email";
		List<Usuario> users = em.createQuery(jpql,Usuario.class).setParameter("email", email).getResultList();
		
		if(users == null || users.size() == 0){
			return null;
		}else{
			return users.get(0);
		}
	}

	public Usuario salva(Usuario usuario) {
		em.persist(usuario);
		return usuario;
	}
	
	public Usuario atualiza(Usuario usuario) {
		em.merge(usuario);
		return usuario;
	}
	
	public void deleta(Usuario usuario){
		em.remove(usuario);
	}
	
	


	public boolean existeUsuarioOrEmail(String username, String email) {
		String jpql = "select u from Usuario u where u.login = :login or u.email = :email";
		List<Usuario> users = em.createQuery(jpql,Usuario.class).setParameter("login", username).setParameter("email", email).getResultList();
		
		if(users.size() > 0){
			System.out.println("Usuario existe ");
			return true;
		}else{
			System.out.println("Usuario não existe ");
			return false;
		}
		
		
		
	}
}

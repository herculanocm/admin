package com.cunha.admin.models;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class GrupoCliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	

	@NotNull(message="Nome não pode ser nulo.")
	@Column(name="nome")
	@Length(min =6,message="O nome deve ser acima de 6 letras.")
	private String nome;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "GrupoCliente [id=" + id + ", nome=" + nome + "]";
	}
	
	


}

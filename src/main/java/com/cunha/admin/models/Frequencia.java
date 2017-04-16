package com.cunha.admin.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Frequencia {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Campanha campanha;
	
	@NotNull(message="O dia não pode ser nulo.")
	@Column(name="dia")
	private int dia;
	
	@OneToOne
	private Cliente cliente;
	
	@OneToOne
	private Funcionario funcionario;

}

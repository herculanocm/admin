package com.cunha.admin.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Campanha {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message="Nome da campanha não pode ser nulo.")
	@Column(name="nome")
	@Length(min =6,message="O nome da campanha deve ser acima de 6 letras.")
	private String nome;
	
	@NotNull(message="Data Inicio da campanha não pode ser nulo.")
	@Column(name="dtInicio")
	private Calendar dtInicio;
	
	@NotNull(message="Data fim da campanha não pode ser nulo.")
	@Column(name="dtFim")
	private Calendar dtFim;
	
	@Column(name="descricao")
	private String descricao;
	
	@NotNull(message="O Status da campanha não pode ser nulo.")
	@Column(name="flgStatus")
	private boolean flgStatus;
	
	
	
}

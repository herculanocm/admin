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
public class Almoxarifado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message="Nome do almoxarifado não pode ser nulo.")
	@Column(name="nome")
	@Length(min =6,message="O nome do almoxarifado deve ser acima de 6 letras.")
	private String nome;
	
	@Column(name="dtInclusao")
	private Calendar dtInclusao;
}

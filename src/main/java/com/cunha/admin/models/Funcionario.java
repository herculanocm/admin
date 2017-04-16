package com.cunha.admin.models;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Funcionario {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Usuario usuario;
	
	@NotNull(message="Nome não pode ser nulo.")
	@Column(name="nome")
	@Length(min =10,message="O Nome deve ser acima de 10 letras.")
	private String nome;
	
	@Column(name="cpf")
	private String cpf;
	
	@Column(name="dtInicio")
	private Calendar dtInclusao;
	
	@Column(name="flgVendedor")
	private boolean flgVendedor;
	
	@NotNull(message="Logradourdo não pode ser nulo.")
	@Column(name="logradouro")
	@Length(min =10,message="O logradouro deve ser acima de 10 letras.")
	private String logradouro;
	
	
	@Column(name="complemento")
	private String complemento;
	
	@Column(name="complemento2")
	private String complemento2;
	
	@NotNull
	@Column(name="bairro")
	@Length(min =4)
	private String bairro;
	
	@NotNull(message="Cidade não pode ser nulo.")
	@Column(name="cidade")
	@Length(min =5)
	private String cidade;
	
	@NotNull(message="Estado não pode ser nulo.")
	@Column(name="estado")
	@Length(min =2)
	private String estado;
	
	@NotNull(message="Numero não pode ser nulo.")
	@Column(name="numero")
	private int numero;
	
	@NotNull(message="Cep não pode ser nulo.")
	@Column(name="cep")
	private int cep;
	
	@Column(name="descricao")
	private String descricao;
	
	
	@NotNull(message="Fone principal não pode ser nulo.")
	@Column(name="fonePrincipal")
	private String fonePrincipal;
	
	@Column(name="foneResidencial")
	private String foneResidencial;
	
	@Column(name="email")
	private String email;
	

	@Column(name="flgStatus")
	private boolean flgStatus;



}

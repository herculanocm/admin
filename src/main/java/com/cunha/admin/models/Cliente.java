package com.cunha.admin.models;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Entity
public class Cliente{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Usuario usuario;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private GrupoCliente grupoCliente;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Funcionario> funcionario = new ArrayList<>();
	
	@OneToOne
	private Funcionario vendedorPrincipal;
	
	
	@NotNull(message="Nome não pode ser nulo.")
	@Column(name="nome")
	@Length(min =10,message="O Nome deve ser acima de 10 letras.")
	private String nome;
	
	@NotNull(message="Fantasia não pode ser nulo.")
	@Column(name="fantasia")
	@Length(min =10,message="A Fantasia deve ser acima de 10 letras.")
	private String fantasia;
	
	

	@NotNull(message="CGC não pode ser nulo.")
    @Size(min=1, max=255)
	@Column(name="cgc")
	private String cgc;
	
	
	@Column(name="ie")
	private String ie;
	
	
	@Column(name="clienteTipo")
	private ClienteTipo clienteTipo;
	
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
	
	@Column(name="foneComercial")
	private String foneComercial;
	

	@Column(name="email")
	private String email;
	
	@Column(name="dtInclusao")
	private Calendar dtInclusao;
	
	@NotNull(message="O Status não pode ser nulo.")
	@Column(name="flgStatus")
	private boolean flgStatus;
	
	
	
	
	
}

package com.cunha.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		System.out.println("Iniciando Aplicacao");
		SpringApplication.run(AdminApplication.class, args);
	}
}

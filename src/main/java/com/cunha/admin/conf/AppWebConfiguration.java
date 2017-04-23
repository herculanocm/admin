package com.cunha.admin.conf;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@EnableWebMvc
public class AppWebConfiguration extends WebMvcConfigurerAdapter {

	
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/*
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	*/
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
	    return new CommonsMultipartResolver();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize("500MB");
	    factory.setMaxRequestSize("500MB");
	    return factory.createMultipartConfig();
	}
	
	
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
	}
	
	/*
	
	@Bean
	public MailSender mailSender() {
	JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
	javaMailSenderImpl.setHost("smtp.gmail.com");
	javaMailSenderImpl.setPassword("yeshua63");
	javaMailSenderImpl.setPort(587);
	javaMailSenderImpl.setUsername("herculano.cunha2");
	Properties mailProperties = new Properties();
	mailProperties.put("mail.smtp.auth", true);
	mailProperties.put("mail.smtp.starttls.enable", true);
	javaMailSenderImpl.setJavaMailProperties(mailProperties);
	return javaMailSenderImpl;
	}
	*/
	
	
}

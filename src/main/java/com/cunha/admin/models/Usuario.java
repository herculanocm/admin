package com.cunha.admin.models;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
  
@Entity
public class Usuario implements UserDetails {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="O login não pode ser nulo.")
	@Column(name="login")
	@Id
	private String login;
	
	@NotNull(message="O pasword não pode ser nulo.")
	@Column(name="password")
	private String password;
	
	@NotNull(message="O nome não pode ser nulo.")
	@Column(name="name")
	private String name;
	
	@NotNull(message="O email não pode ser nulo.")
	@Column(name="email")
	private String email;
	
	@Column(name="erros")
	private int erros;
	
	@Column(name="img")
	@Lob
	private byte[] img;
	
	@Column(name="img_ext")
	private String imgExt;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<>();
	
	@Column(name="isAccountNonExpired")
	private boolean isAccountNonExpired;
	
	@Column(name="isAccountNonLocked")
	private boolean isAccountNonLocked;
	
	@Column(name="isCredentialsNonExpired")
	private boolean isCredentialsNonExpired;
	  
	@Column(name="isEnabled")
	private boolean isEnabled;
	
	
	public int getErros() {
		return erros;
	}

	public void setErros(int erros) {
		this.erros = erros;
	}
	

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
	
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	

	public String getImgExt() {
		return imgExt;
	}

	public void setImgExt(String imgExt) {
		this.imgExt = imgExt;
	}

	@Override
	public String toString() {
		return "Usuario [login=" + login + ", password=" + password + ", name=" + name + ", email=" + email + ", erros="
				+ erros + ", img=" + Arrays.toString(img) + ", imgExt=" + imgExt + ", roles=" + roles
				+ ", isAccountNonExpired=" + isAccountNonExpired + ", isAccountNonLocked=" + isAccountNonLocked
				+ ", isCredentialsNonExpired=" + isCredentialsNonExpired + ", isEnabled=" + isEnabled + "]";
	}

	



	

	
	
	

}

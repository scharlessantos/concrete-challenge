package br.dev.charles.challenge.controller.resources;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class UserProfileResource  {

	private UUID id;
	private String name;
	private String email;
	private String password;
	
	private List<UserPhoneResource> phones;
	
	private Date created;
	private Date modified;
	private Date last_login;
	private String token;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<UserPhoneResource> getPhones() {
		return phones;
	}
	public void setPhones(List<UserPhoneResource> phones) {
		this.phones = phones;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getLast_login() {
		return last_login;
	}
	public void setLast_login(Date lastLogin) {
		this.last_login = lastLogin;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}


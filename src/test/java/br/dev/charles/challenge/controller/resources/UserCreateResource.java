package br.dev.charles.challenge.controller.resources;

import java.util.ArrayList;
import java.util.List;

public class UserCreateResource {

	private String name;
	private String email;
	private String password;
	private List<UserPhoneResource> phones;

	public String getName() {
		return name;
	}

	public UserCreateResource setName(String name) {
		this.name = name;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserCreateResource setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserCreateResource setPassword(String password) {
		this.password = password;
		return this;
	}

	public List<UserPhoneResource> getPhones() {
		return phones;
	}

	public UserCreateResource setPhones(List<UserPhoneResource> phones) {
		this.phones = phones;
		return this;
	}
	
	public UserCreateResource addUserPhoneResource(UserPhoneResource phone) {
		if (phones == null)
			phones = new ArrayList<>();
		phones.add(phone);
		return this;
	}
	
}

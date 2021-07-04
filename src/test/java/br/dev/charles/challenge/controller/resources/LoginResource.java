package br.dev.charles.challenge.controller.resources;

public class LoginResource {
	
	private String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public LoginResource setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public LoginResource setPassword(String password) {
		this.password = password;
		return this;
	}

}

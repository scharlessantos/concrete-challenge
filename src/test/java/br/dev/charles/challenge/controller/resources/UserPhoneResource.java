package br.dev.charles.challenge.controller.resources;

public class UserPhoneResource {

	private String ddd;
	private String number;
	
	public UserPhoneResource() {
	}
	
	public UserPhoneResource(String ddd, String number) {
		this.ddd = ddd;
		this.number = number;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
}

package br.dev.charles.challenge.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserPhone implements Serializable {

	private static final long serialVersionUID = -9012796057835832233L;

	private String ddd;
	private String number;

	public UserPhone() {
		
	}
	
	public UserPhone(String ddd, String number) {
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

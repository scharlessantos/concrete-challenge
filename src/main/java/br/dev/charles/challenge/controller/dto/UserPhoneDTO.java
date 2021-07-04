package br.dev.charles.challenge.controller.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserPhoneDTO {

	@NotNull @NotBlank @NotEmpty
	private String ddd;
	
	@NotNull @NotBlank @NotEmpty
	private String number;
	
	public UserPhoneDTO() {
	}
	
	public UserPhoneDTO(String ddd, String number) {
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

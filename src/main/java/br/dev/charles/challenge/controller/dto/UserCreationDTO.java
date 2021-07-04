package br.dev.charles.challenge.controller.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserCreationDTO {

	@NotNull @NotEmpty
	private String name;
	
	@NotNull @NotEmpty @NotBlank @Pattern(regexp = ".+@.+\\.[a-zA-Z]{2,}")
	private String email;
	
	@NotNull @NotEmpty
	private String password;
	
	private List<UserPhoneDTO> phones;

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

	public List<UserPhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(List<UserPhoneDTO> phones) {
		this.phones = phones;
	}
	
	
}

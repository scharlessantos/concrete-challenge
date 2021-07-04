package br.dev.charles.challenge.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserUpdateDTO extends UserCreationDTO {
	
	@NotNull
	private UUID id;
	private String name;
	
	@Pattern(regexp = ".+@.+\\.[a-zA-Z]{2,}")
	private String email;
	private String password;
	
	private List<UserPhoneDTO> phones;

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

	public List<UserPhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(List<UserPhoneDTO> phones) {
		this.phones = phones;
	}
	
	public void addPhone(UserPhoneDTO phone) {
		if (phones == null)
			phones = new ArrayList<>();
		
		phones.add(phone);
	}


	
}

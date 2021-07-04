package br.dev.charles.challenge.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.charles.challenge.controller.dto.LoginDTO;
import br.dev.charles.challenge.controller.dto.UserProfileDTO;
import br.dev.charles.challenge.error.ErrorMessage;
import br.dev.charles.challenge.model.User;
import br.dev.charles.challenge.repository.UserRepository;
import br.dev.charles.challenge.utils.StringUtils;

@RestController
@RequestMapping(value = "/login", produces = "application/json; charset=UTF-8")
public class LoginController {

	protected static final ModelMapper mapper = new ModelMapper();
	
	@Autowired
	private UserRepository repository;
	
	@PostMapping
	public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) {
		User u = repository.getByEmail(login.getEmail());
		if (u == null)
			return new ResponseEntity<>(new ErrorMessage("Usuário/Senha inválido"), HttpStatus.FORBIDDEN);
		
		if (!u.getPassword().equals(StringUtils.sha256(login.getPassword())))
			return new ResponseEntity<>(new ErrorMessage("Usuário/Senha inválido"), HttpStatus.FORBIDDEN);
		
		u.updateToken();
		repository.save(u);
		return ResponseEntity.ok(mapper.map(u, UserProfileDTO.class));
	}
	
	
}

package br.dev.charles.challenge.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.dev.charles.challenge.controller.dto.UserCreationDTO;
import br.dev.charles.challenge.controller.dto.UserProfileDTO;
import br.dev.charles.challenge.controller.dto.UserUpdateDTO;
import br.dev.charles.challenge.error.ErrorMessage;
import br.dev.charles.challenge.model.User;
import br.dev.charles.challenge.repository.UserRepository;
import br.dev.charles.challenge.utils.StringUtils;

@RestController
@RequestMapping(value = "/user", produces = "application/json; charset=UTF-8")
public class UserController {
	
	protected static final ModelMapper mapper = new ModelMapper();
	
	@Autowired
	private UserRepository repository;

	@PostMapping
	public UserProfileDTO create(@RequestBody @Valid UserCreationDTO user) {
		return mapper.map(repository.save(mapper.map(user, User.class)), UserProfileDTO.class);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestHeader(value = "token", required = false) String token, @RequestBody UserUpdateDTO userDTO) {
		ResponseEntity<?> e = tokenValidation(token);
		if (e != null)
			return e;
		
		User u = repository.getById(userDTO.getId());
		if (u == null)
			return new ResponseEntity<>(new ErrorMessage("Usuário não encontrado"), HttpStatus.NOT_FOUND);
		
		User user = mapper.map(userDTO, User.class);
		
		if (user.getPassword() != null)
			u.setPassword(StringUtils.sha256(user.getPassword()));
		
		if (user.getEmail() != null)
			u.setEmail(user.getEmail());
		
		if (user.getEmail() != null)
			u.setEmail(user.getEmail());
		
		if (user.getPhones() != null && !user.getPhones().isEmpty())
			u.setPhones(user.getPhones());
		
		return ResponseEntity.ok(repository.save(u));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@RequestHeader(value = "token", required = false) String token, @PathVariable(value = "id") UUID id){
		ResponseEntity<?> e = tokenValidation(token);
		if (e != null)
			return e;
		
		Optional<User> u = repository.findById(id);
		if (u.isEmpty())
			return new ResponseEntity<>(new ErrorMessage("Usuário não encontrado"), HttpStatus.NOT_FOUND);
		
		repository.delete(u.get());
		return ResponseEntity.ok(Map.of("deleted", true));
	}
	
	@GetMapping("/profile/{id}")
	public ResponseEntity<?> profile(@RequestHeader(value = "token", required = false) String token, @PathVariable(value = "id") UUID id){
		ResponseEntity<?> e = tokenValidation(token);
		if (e != null)
			return e;
		
		Optional<User> u = repository.findById(id);
		if (u.isEmpty())
			return new ResponseEntity<>(new ErrorMessage("Usuário não encontrado"), HttpStatus.NOT_FOUND);
		
		return ResponseEntity.ok(mapper.map(u.get(), UserProfileDTO.class));
	}
	
	private ResponseEntity<?> tokenValidation(String token) {
		if (token == null || token.isBlank())
			return new ResponseEntity<>(new ErrorMessage("Não autorizado"), HttpStatus.UNAUTHORIZED);
		
		User u = repository.getByToken(token);
		if (u == null)
			return new ResponseEntity<>(new ErrorMessage("Não autorizado"), HttpStatus.UNAUTHORIZED);
		
		if (u.getLast_login().before(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30))))
			return new ResponseEntity<>(new ErrorMessage("Sessão inválida"), HttpStatus.UNAUTHORIZED);
		
		return null;
	}
	
}

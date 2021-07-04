package br.dev.charles.challenge.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.charles.challenge.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	User getByEmail(String email);
	
	User getByToken(String token);
}

package br.dev.charles.challenge.error;

import java.util.TreeSet;

import javax.validation.ConstraintViolationException;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleParserError(MethodArgumentNotValidException ex) {
		TreeSet<String> fields = new TreeSet<>();
		
		ex.getBindingResult().getFieldErrors().forEach(e -> {
			fields.add(e.getField());
		});
		
		StringBuilder message = new StringBuilder();
		message.append(fields.size() == 1? "Campo " : "Campos ");
		message.append(String.join(", ", fields));
		message.append(fields.size() == 1 ? " inválido" : " inválidos");
		
		return new ResponseEntity<>(new ErrorMessage(message.toString()), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
	public ResponseEntity<?> handleDuplicatedUser(JdbcSQLIntegrityConstraintViolationException ex) {
		return new ResponseEntity<>(new ErrorMessage("Email já cadastrado"), HttpStatus.CONFLICT);	
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleDuplicatedUser(ConstraintViolationException ex) {
		return new ResponseEntity<>(new ErrorMessage("Email já cadastrado"), HttpStatus.CONFLICT);	
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		return new ResponseEntity<>(new ErrorMessage("Método não suportado"), HttpStatus.METHOD_NOT_ALLOWED);	
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericError(Exception ex) {
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);	
	}
}


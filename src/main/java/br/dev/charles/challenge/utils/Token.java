package br.dev.charles.challenge.utils;

import javax.persistence.Embeddable;

@Embeddable
public class Token {
	
	private String token;
	
	public Token() {
		
	}
	
	public Token(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return token;
	}
	
	public static Token of(String token) {
		return new Token(StringUtils.sha256(token));
	}
}

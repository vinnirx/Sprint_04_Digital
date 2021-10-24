package br.com.fiap.challenge.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.fiap.challenge.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenService {

	@Value("${sprint.jwt.duration}")
	private long duration;
	
	@Value("${sprint.jwt.secret}")
	private String secret;

	public String createToken(Authentication authenticate) {
		User user = (User) authenticate.getPrincipal();
		
		Date today = new Date();
		
		Date expiration = new Date(today.getTime() + duration);
		
		
		String token = Jwts.builder()
							.setIssuer("SprintAPI")
							.setSubject(user.getId().toString())
							.setIssuedAt(today)
							.setExpiration(expiration)
							.signWith(SignatureAlgorithm.HS256, secret)
							.compact();
		return token;
	}

	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.valueOf( claims.getSubject() );
	}

}

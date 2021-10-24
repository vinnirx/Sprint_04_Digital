package br.com.fiap.challenge.controller.api;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.challenge.model.User;
import br.com.fiap.challenge.repository.UserRepository;
import br.com.fiap.challenge.service.TokenService;

public class AuthorizationFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	
	private UserRepository repository;
	
	public AuthorizationFilter(TokenService tokenService, UserRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(
				HttpServletRequest request, 
				HttpServletResponse response, 
				FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = extractToken(request);
		
		boolean valid = tokenService.validate(token);
		
		if (valid) authenticate(token);
		
		filterChain.doFilter(request, response);
		
	}

	private void authenticate(String token) {
		Long id = tokenService.getUserId(token);
		
		Optional<User> optional = repository.findById(id);
		
		if(optional.isPresent()) {
			User user = optional.get();
			
			UsernamePasswordAuthenticationToken auth = 
					new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(auth);		
		}
		
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		if (header == null || !header.startsWith("Bearer "))
			return null;
		
		return header.substring(7, header.length());
		
	}

}

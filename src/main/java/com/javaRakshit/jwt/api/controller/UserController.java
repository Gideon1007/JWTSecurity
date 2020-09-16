package com.javaRakshit.jwt.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javaRakshit.jwt.api.models.AuthenticationRequest;
import com.javaRakshit.jwt.api.models.AuthenticationResponse;
import com.javaRakshit.jwt.api.service.JwtUtil;
import com.javaRakshit.jwt.api.service.MyUserDetailsService;

@RestController
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping({"/hello"})
	public String hello() {
		return "Hello World";
	}

	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception{
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
						authenticationRequest.getPassword())
				);
		}catch(BadCredentialsException e){
			throw new Exception("invalid username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new  AuthenticationResponse(jwt));
			}
}


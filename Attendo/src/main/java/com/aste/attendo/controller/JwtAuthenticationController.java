package com.aste.attendo.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aste.attendo.dao.AppUserDao;
import com.aste.attendo.model.AppUser;
import com.aste.attendo.model.JwtRequest;
import com.aste.attendo.model.JwtResponse;
import com.aste.attendo.security.JwtTokenUtil;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	@Autowired
	AppUserDao appUserDao;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
	
		
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		String role = appUserDao.findByNRIC(authenticationRequest.getUsername()).getRole();
		return ResponseEntity.ok(new JwtResponse(token,role));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@PostMapping("/register")
	private ResponseEntity<?> register(@RequestBody JwtRequest register) {
		
		AppUser appUser = appUserDao.findByNRIC(register.getUsername());
		if(appUser!= null){
			String password = new BCryptPasswordEncoder().encode(register.getPassword());
			appUser.setPassword(password);
			appUserDao.save(appUser);
			System.err.println("i m good");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}
}

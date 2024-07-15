package com.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.dto.LoginRequest;
import com.attendance.model.User;
import com.attendance.security.JWTService;
import com.attendance.service.UserService;
import com.expense.model.DTO.LoginResponseDTO;




@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	JWTService jwtService;
	
	

	
	@PostMapping("/signup")
	public ResponseEntity<?> register(@RequestBody User user) {
		
		userService.saveUser(user);
		
		return ResponseEntity.ok("Succsefully signup");
	}
	

		@PostMapping("/login")
		public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequest loginRequest) {
			User authenticatedUser = userService.authenticate(loginRequest);

			String jwtToken = jwtService.generateToken(authenticatedUser);

			LoginResponseDTO loginResponse = new LoginResponseDTO();
			loginResponse.setToken(jwtToken);
			loginResponse.setExpiresIn(jwtService.getExpirationTime());

			return ResponseEntity.ok(loginResponse);
		}
		
		
	
	

}

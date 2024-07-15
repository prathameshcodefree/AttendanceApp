package com.attendance.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.model.User;
import com.attendance.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	  @GetMapping
	    public ResponseEntity<?> getAllUsers() {
		  
		  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		  
		  if(!user.getRole().equalsIgnoreCase("ADMIN")) {
			  return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("this user is not allowed to access this resource");
		  }
		  
		  System.out.println("user " + user.getRole());
		  
	        List<User> users = userService.getAllUsers();
	        return ResponseEntity.ok(users);
	    }
	  
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getUserById(@PathVariable Long id) {
	        Optional<User> user = userService.getUserById(id);

	        return user.map(ResponseEntity::ok)
	                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
	    }
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
	        userService.deleteUserById(id);
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }

}

package com.attendance.service;

import com.attendance.model.User;
import com.attendance.repository.UserRepository;
import com.attendance.security.JWTService;
import com.attendance.util.PasswordEncoderUtil;
import com.attendance.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        

        System.out.println("User found with username: " + username);
        
        return user;
    }

    private AuthenticationManager getAuthenticationManager() {
        if (authenticationManager == null) {
            authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        }
        return authenticationManager;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoderUtil.encodePassword(user.getPassword()));
        userRepository.save(user);
    }

    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && passwordEncoderUtil.matches(password, user.getPassword());
    }

    public User login(LoginRequest loginRequestDto) {
        Optional<User> UserO = userRepository.findUserByUsername(loginRequestDto.getUsername());

        User user = null;

        if (UserO.isPresent()) {
            User userdb = UserO.get();
            if (passwordEncoderUtil.matches(loginRequestDto.getPassword(), userdb.getPassword())) {
                user = userdb;
            }
        }

        return user;
    }

    public User authenticate(LoginRequest loginRequest) {
    	
    	System.out.println("auth function 1");
        UserDetails user = (UserDetails) getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()
                )).getPrincipal();

    	System.out.println("auth function 2");
        

        return userRepository.findByUsername(user.getUsername());
    }
}

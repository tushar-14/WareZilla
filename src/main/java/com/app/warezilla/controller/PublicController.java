package com.app.warezilla.controller;

import com.app.warezilla.dto.UserLoginDto;
import com.app.warezilla.model.User;
import com.app.warezilla.service.UserDetailsServiceImp;
import com.app.warezilla.service.UserService;
import com.app.warezilla.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public")
@Slf4j
public class PublicController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImp userDetailsServiceImp;
    private JwtUtil jwtUtill;

    public PublicController(UserService userService, AuthenticationManager authenticationManager, UserDetailsServiceImp userDetailsServiceImp, JwtUtil jwtUtill) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtUtill = jwtUtill;
    }

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;


    @GetMapping("/check")
    public String systemCheck() {
        return "app is running...";
    }

    @PostMapping("/signup")
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto user){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));

            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(user.getUserName());

            return new ResponseEntity<>(jwtUtill.generateToken(userDetails.getUsername()), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Invalid login attempt for user: {}", user.getUserName(),e);
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}

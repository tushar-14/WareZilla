package com.app.warezilla.controller;

import com.app.warezilla.model.User;
import com.app.warezilla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user){

        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.updateUser(authentication,user);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(){

        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.deleteUserByUserName(authentication);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}

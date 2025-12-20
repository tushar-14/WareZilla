package com.app.warezilla.controller;

import com.app.warezilla.model.User;
import com.app.warezilla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers(){
        try{
            return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdminUser(@RequestBody User user){
        try{
            userService.createAdmin(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

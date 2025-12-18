package com.app.warezilla.controller;

import com.app.warezilla.model.User;
import com.app.warezilla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }

}

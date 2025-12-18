package com.app.warezilla.service;

import com.app.warezilla.model.User;
import com.app.warezilla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public void deleteUserById(long id){
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByUserName(Authentication authentication){
        String userName = authentication.getName();
        userRepository.deleteByUserName(userName);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public void updateUser(Authentication authentication ,User user){

        User userIndb = findByUserName(authentication.getName());

        if(user!= null){
            userIndb.setUserName(user.getUserName());
            userIndb.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }
}

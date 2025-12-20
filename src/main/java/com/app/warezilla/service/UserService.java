package com.app.warezilla.service;

import com.app.warezilla.model.User;
import com.app.warezilla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void createAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
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
    public void deleteUserByUserName(String userName){
        userRepository.deleteByUserName(userName);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public void updateUser(String userName ,User user){

        User userIndb = findByUserName(userName);

        if(user!= null){
            userIndb.setUserName(user.getUserName());
            userIndb.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }
}

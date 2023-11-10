package com.georgeifrim.Spring_task1.controller;

import com.georgeifrim.Spring_task1.dao.UserDao;
import com.georgeifrim.Spring_task1.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User createdUser = userDao.createUser(user);
        return createdUser;
    }

    @GetMapping("/allUsers")
    public TreeMap<Integer, User> getAllUsers(){
        return userDao.getUsers();
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable int id,
                           @RequestBody User user){
        return userDao.updateUser(id, user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public User deleteUser(@PathVariable int id){
        return userDao.deleteUser(id);
    }
}

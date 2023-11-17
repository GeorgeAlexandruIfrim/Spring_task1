package com.georgeifrim.Spring_task1.controller;

import com.georgeifrim.Spring_task1.dao.Dao;
import com.georgeifrim.Spring_task1.dao.UserDao;
import com.georgeifrim.Spring_task1.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class UserController {

    private final Dao<Integer, User> userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        User createdUser = userDao.create(user);
        return createdUser;
    }

    @GetMapping("/users")
    public HashMap<Integer, User> getAllUsers(){
        return userDao.getAll();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id,
                           @RequestBody User user){
        return userDao.update(id, user);
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable int id){
        return userDao.delete(id);
    }
}

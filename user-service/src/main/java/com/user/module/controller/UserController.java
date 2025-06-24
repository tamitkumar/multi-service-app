package com.user.module.controller;

import com.user.module.model.UserEntity;
import com.user.module.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.save(user);
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.findAll();
    }
}

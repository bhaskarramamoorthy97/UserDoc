package com.springsecurity.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.springsecurity.dto.Users;
import com.springsecurity.springsecurity.service.SpringUsersServiceImpl;

@RestController
public class UserController {

    @Autowired
    private SpringUsersServiceImpl springUsersServiceImpl;

    @PostMapping("/login")
    public String login(@RequestBody Users users) {
        return springUsersServiceImpl.login(users);

    }

}

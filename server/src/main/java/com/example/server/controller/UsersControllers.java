package com.example.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.service.UserService;
import com.example.server.model.User;
import java.util.List;

@RestController
public class UsersControllers {
    

    private final UserService svc;

    public UsersControllers(UserService svc){
        this.svc = svc;
    }

    @GetMapping("/allusers")
    public List<User> allusers(){
        return svc.getAllUsers();
    }
}

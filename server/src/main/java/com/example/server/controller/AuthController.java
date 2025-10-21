
package com.example.server.controller;

import java.util.HashMap;
// import java.util.HashMap;
import java.util.Map;
import java.util.List;

// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.server.model.LoginRequest;
import com.example.server.model.User;
import com.example.server.service.UserService;
// import com.example.server.validation.*;

import jakarta.validation.Valid;;

@RestController
public class AuthController {
    private final UserService svc;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService svc, PasswordEncoder passwordEncoder) {
        this.svc = svc;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/allUsers")
    // public List<User> all() {
    //     return svc.getAllUsers();
    // }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User u) {
        Map<String, Object> res = new HashMap<>();

        User user = svc.getByEmail(u.getEmail());
        if (user != null) {
            res.put("status", "error");
            res.put("message", "Already exist user, please login");
            return ResponseEntity.status(409).body(res); 
        } else {
            System.out.println("IIIIIII");
            User us = svc.create(u);
            res.put("status", "ok");
            res.put("message", "User created successfully");
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", us.getId());
            userData.put("email", us.getEmail());
            res.put("user", userData);
            return ResponseEntity.status(201).body(res);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest req) {
        Map<String, String> res = new HashMap<>();

        User user = svc.getByEmail(req.getEmail());
        if (user == null) {
            res.put("status", "error");
            res.put("message", "Not such as user ");
            return ResponseEntity.ok(res);
        }
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            res.put("status", "ok");
            res.put("message", "Logged in");
            return ResponseEntity.ok(res);
        } else {
            res.put("status", "error");
            res.put("message", "Wrong password");
            return ResponseEntity.ok(res);
        }

    }

    // @PutMapping("/user/{id}")
    // public User updateUser(@RequestBody User newU,
    // @Validated(ValidationGroups.OnUpdate.class) @PathVariable Long id) {
    // return svc.updateUser(id, newU);
    // }

    // @DeleteMapping("/delete/{id}")
    // public ResponseEntity<Void> delete(@PathVariable Long id) {
    // System.out.println("JJJJ=>" + id);
    // svc.deleteUser(id);
    // return ResponseEntity.ok().build();
    // }

}

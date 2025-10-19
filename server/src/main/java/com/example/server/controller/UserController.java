
package com.example.server.controller;
import java.util.HashMap;
// import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.server.model.User;
import com.example.server.service.UserService;
import com.example.server.validation.*;

import jakarta.validation.Valid;;

@RestController
public class UserController {
    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @GetMapping("/allUsers")
    public List<User> all() {
        return svc.getAllUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> create(@Validated(ValidationGroups.OnCreate.class) @RequestBody User u) {
        User saved = svc.create(u);
        return ResponseEntity.status(201).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody User u) {
        Map<String, String> res = new HashMap<>();

        User user = svc.getByEmail(u.getEmail());
        if (user != null && user.getPassword().equals(u.getPassword())) {
            System.out.println("NNNNNNNNNN");
            res.put("status", "ok");
            res.put("message", "Logged in");
            return ResponseEntity.ok(res);
        } else {
            System.out.println("OOOOOO");
            res.put("status", "error");
            res.put("message", "Not Logged in");
            return ResponseEntity.ok(res);
        }

    }

    // @PutMapping("/user/{id}")
    // public User updateUser(@RequestBody User newU, @Validated(ValidationGroups.OnUpdate.class) @PathVariable Long id) {
    //     return svc.updateUser(id, newU);
    // }

    // @DeleteMapping("/delete/{id}")
    // public ResponseEntity<Void> delete(@PathVariable Long id) {
    //     System.out.println("JJJJ=>" + id);
    //     svc.deleteUser(id);
    //     return ResponseEntity.ok().build();
    // }

}

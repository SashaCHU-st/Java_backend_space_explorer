package com.example.server.controller;

import com.example.server.model.User;
import com.example.server.service.UserService;
import com.example.server.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ProfileController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PutMapping("/editProfile")
    public ResponseEntity<Map<String, Object>> editProfile(
            @RequestBody User updateUser,
            HttpServletRequest request) {

        Map<String, Object> res = new HashMap<>();

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || !jwtUtil.isValid(token)) {
            res.put("status", "error");
            res.put("message", "Unauthorized — invalid or missing token");
            return ResponseEntity.status(401).body(res);
        }
        String email = jwtUtil.extractEmail(token);
        User userFromDb = userService.getByEmail(email);
        if (userFromDb == null) {
            res.put("status", "error");
            res.put("message", "User not found");
            return ResponseEntity.status(404).body(res);
        }

        User updatedUser = userService.updateUser(userFromDb, updateUser);

        res.put("status", "ok");
        res.put("message", "Profile updated successfully");
        res.put("user", Map.of(
                "id", updatedUser.getId(),
                "name", updatedUser.getName(),
                "email", updatedUser.getEmail()));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || !jwtUtil.isValid(token)) {
            res.put("status", "error");
            res.put("message", "Unauthorized — invalid or missing token");
            return ResponseEntity.status(401).body(res);
        }
        String email = jwtUtil.extractEmail(token);
        User user = userService.getByEmail(email);
        if (user == null) {
            res.put("status", "error");
            res.put("message", "User not found");
            return ResponseEntity.status(404).body(res);
        }

        res.put("status", "ok");
        res.put("user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
                // "password", user.getPassword()
                ));

        return ResponseEntity.ok(res);
    }

}

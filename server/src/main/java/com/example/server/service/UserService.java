package com.example.server.service;

import com.example.server.repository.AuthRepository;
import com.example.server.model.User;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AuthRepository repo;

    public UserService(AuthRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User create(User u) {
        System.out.println("IIIIIII" + u.getName());
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User updateUser(Long id, User updateUser) {
        return repo.findById(id)
                .map(user -> {
                    if (updateUser.getName() != null && !updateUser.getName().isBlank()) {
                        user.setName(updateUser.getName());
                    }
                    if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
                    }
                    return repo.save(user);
                })
                .orElse(null);
    }

}

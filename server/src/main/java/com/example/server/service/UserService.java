package com.example.server.service;

import com.example.server.repository.UserRepository;
import com.example.server.model.User;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User create(User u) {
        return repo.save(u);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this name not found"));
    }

    public User updateUser(Long id, User updateUser) {
        return repo.findById(id)
                .map(user -> {
                    if (updateUser.getName() != null && !updateUser.getName().isBlank()) {
                        user.setName(updateUser.getName());
                    }
                    if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
                        user.setPassword(updateUser.getPassword());
                    }
                    return repo.save(user);
                })
                .orElse(null);
    }

}

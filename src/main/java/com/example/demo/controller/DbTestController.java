package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dbtest")
public class DbTestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Database controller is up");
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found with id: " + id);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping(value = "/users", 
                 consumes = MediaType.APPLICATION_JSON_VALUE, 
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping(value = "/test-create-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createTestUser() {
        User testUser = new User();
        testUser.setUsername("testuser" + System.currentTimeMillis());
        testUser.setPassword("password123");
        testUser.setEmail("test" + System.currentTimeMillis() + "@example.com");
        testUser.setRole("USER");
        
        User savedUser = userRepository.save(testUser);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> countUsers() {
        long count = userRepository.count();
        Map<String, Long> response = new HashMap<>();
        response.put("userCount", count);
        return ResponseEntity.ok(response);
    }
}

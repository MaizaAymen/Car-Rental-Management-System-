package com.example.demo.service;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    public UserDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = modelMapper.map(userCreateDto, User.class);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public Optional<UserDto> updateUser(Long id, UserCreateDto userCreateDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Check if username is being changed and if it already exists
                    if (!existingUser.getUsername().equals(userCreateDto.getUsername()) && 
                            userRepository.existsByUsername(userCreateDto.getUsername())) {
                        throw new RuntimeException("Username already exists");
                    }
                    
                    // Check if email is being changed and if it already exists
                    if (!existingUser.getEmail().equals(userCreateDto.getEmail()) && 
                            userRepository.existsByEmail(userCreateDto.getEmail())) {
                        throw new RuntimeException("Email already exists");
                    }
                    
                    existingUser.setUsername(userCreateDto.getUsername());
                    existingUser.setEmail(userCreateDto.getEmail());
                    existingUser.setRole(userCreateDto.getRole());
                    
                    // Update password only if it's not null or empty
                    if (userCreateDto.getPassword() != null && !userCreateDto.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
                    }
                    
                    User updatedUser = userRepository.save(existingUser);
                    return modelMapper.map(updatedUser, UserDto.class);
                });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }
}

package com.PlayGround.Coding.PlayGround.CRUD.service.impl;


import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.entity.User;
import com.PlayGround.Coding.PlayGround.CRUD.exception.ResourceNotFoundException;
import com.PlayGround.Coding.PlayGround.CRUD.repository.UserRepository;
import com.PlayGround.Coding.PlayGround.CRUD.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    // Constructor-based dependency injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Attempting to create a new user with email: {}", userDto.getEmail());
        User user = toEntity(userDto);
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return toDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Attempting to find user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        log.info("Successfully found user with ID: {}", userId);
        return toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Attempting to retrieve all users.");
        List<User> users = userRepository.findAll();
        log.info("Successfully retrieved {} users.", users.size());
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Attempting to update user with ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for update with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());

        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", userId);
        return toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Attempting to delete user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("User not found for deletion with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with ID: {}", userId);
    }

    // --- Helper Methods for DTO/Entity Conversion ---

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private User toEntity(UserDto dto) {
        User user = new User();
        // We don't set ID here as it's generated for new entities
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }
}

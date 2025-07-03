package com.PlayGround.Coding.PlayGround.CRUD.controller;

import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * Constructs a UserController with the necessary UserService.
     *
     * @param userService The service layer for user-related business logic.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user based on the provided user data.
     *
     * @param userDto The data transfer object containing the new user's details.
     * @return a {@link ResponseEntity} containing the created {@link UserDto} and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Received request to create user");
        UserDto createdUser = userService.createUser(userDto);
        log.info("Responding with created user, ID: {}", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific user by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return a {@link ResponseEntity} containing the found {@link UserDto} and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        log.info("Received request to get user by ID: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("Responding with user data for ID: {}", userId);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a {@link ResponseEntity} containing a list of all {@link UserDto} objects and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Received request to get all users");
        List<UserDto> users = userService.getAllUsers();
        log.info("Responding with a list of {} users", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * Updates an existing user's information.
     *
     * @param userId  The ID of the user to update.
     * @param userDto The data transfer object containing the updated user details.
     * @return a {@link ResponseEntity} containing the updated {@link UserDto} and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto userDto) {
        log.info("Received request to update user with ID: {}", userId);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        log.info("Responding with updated user data for ID: {}", userId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param userId The ID of the user to delete.
     * @return a {@link ResponseEntity} with a success message and HTTP status 200 (OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        log.info("Received request to delete user with ID: {}", userId);
        userService.deleteUser(userId);
        log.info("User with ID: {} deleted successfully", userId);
        return ResponseEntity.ok("User is successfully deleted.");
    }
}
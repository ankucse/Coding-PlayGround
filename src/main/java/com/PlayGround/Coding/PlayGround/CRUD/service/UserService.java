package com.PlayGround.Coding.PlayGround.CRUD.service;


import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Defines the contract for user management operations.
 * This interface abstracts the business logic for creating, retrieving, updating,
 * and deleting users, providing a clear separation of concerns between the
 * controller and the implementation layer.
 */
public interface UserService {

    /**
     * Creates a new user in the system.
     *
     * @param userDto The data transfer object containing the details of the user to be created.
     * @return The data transfer object of the newly created user, including its generated ID.
     */
    UserDto createUser(UserDto userDto);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique ID of the user to retrieve.
     * @return The data transfer object of the found user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    UserDto getUserById(Long userId);

    /**
     * Retrieves a list of all users currently in the system.
     *
     * @return A list of {@link UserDto} objects representing all users.
     * Returns an empty list if no users exist.
     */
    List<UserDto> getAllUsers();

    /**
     * Updates the information of an existing user.
     *
     * @param userId  The unique ID of the user to update.
     * @param userDto The data transfer object containing the updated details for the user.
     * @return The data transfer object of the updated user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    UserDto updateUser(Long userId, UserDto userDto);

    /**
     * Deletes a user from the system by their unique identifier.
     *
     * @param userId The unique ID of the user to delete.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    void deleteUser(Long userId);
}
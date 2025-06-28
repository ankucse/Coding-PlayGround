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

/**
 * Service implementation for managing users.
 * This class contains the business logic for user-related operations and interacts
 * with the UserRepository to perform data persistence.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Logger for this service implementation.
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * The repository for user data access. Injected via constructor.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new UserServiceImpl with the given UserRepository.
     * Uses constructor-based dependency injection to provide the repository.
     *
     * @param userRepository The repository for user data access.
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * Creates a new user by converting the UserDto to a User entity and saving it to the database.
     *
     * @param userDto The data transfer object containing the details of the user to be created.
     * @return The data transfer object of the newly created user, including the generated ID.
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Attempting to create a new user with email: {}", userDto.getEmail());
        User user = toEntity(userDto);
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return toDto(savedUser);
    }

    /**
     * {@inheritDoc}
     * Retrieves a user by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The data transfer object of the found user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDto getUserById(Long userId) {
        log.info("Attempting to find user with ID: {}", userId);
        // Find the user by ID or throw a ResourceNotFoundException if it doesn't exist.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
        log.info("Successfully found user with ID: {}", userId);
        return toDto(user);
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of all users from the database.
     *
     * @return A list of data transfer objects representing all users. Returns an empty list if no users exist.
     */
    @Override
    public List<UserDto> getAllUsers() {
        log.info("Attempting to retrieve all users.");
        List<User> users = userRepository.findAll();
        log.info("Successfully retrieved {} users.", users.size());
        // Convert the list of User entities to a list of UserDto objects.
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * Updates the details of an existing user identified by their ID.
     *
     * @param userId  The ID of the user to update.
     * @param userDto The data transfer object containing the new details for the user.
     * @return The data transfer object of the updated user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Attempting to update user with ID: {}", userId);
        // First, find the existing user or throw an exception if not found.
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for update with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        // Update the fields of the existing user entity.
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());

        // Save the updated entity back to the database.
        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", userId);
        return toDto(updatedUser);
    }

    /**
     * {@inheritDoc}
     * Deletes a user by their unique ID.
     *
     * @param userId The ID of the user to delete.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public void deleteUser(Long userId) {
        log.info("Attempting to delete user with ID: {}", userId);
        // Check if the user exists before attempting to delete to provide a clear error message.
        if (!userRepository.existsById(userId)) {
            log.error("User not found for deletion with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with ID: {}", userId);
    }

    // --- Helper Methods for DTO/Entity Conversion ---

    /**
     * Converts a {@link User} entity to a {@link UserDto} data transfer object.
     *
     * @param user The user entity to convert.
     * @return The corresponding data transfer object.
     */
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Converts a {@link UserDto} data transfer object to a {@link User} entity.
     *
     * @param dto The data transfer object to convert.
     * @return The corresponding user entity, ready to be persisted.
     */
    private User toEntity(UserDto dto) {
        User user = new User();
        // We don't set the ID here as it's generated by the database for new entities.
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
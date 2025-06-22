package com.PlayGround.Coding.PlayGround.CRUD.nonintegrated;

import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.entity.User;
import com.PlayGround.Coding.PlayGround.CRUD.exception.ResourceNotFoundException;
import com.PlayGround.Coding.PlayGround.CRUD.repository.UserRepository;
import com.PlayGround.Coding.PlayGround.CRUD.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Common setup for tests: create a sample User and UserDto
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
    }

    @DisplayName("Test Create User - Success")
    @Test
    void testCreateUser_Success() {
        // Arrange: Mock the repository's save method to return the saved user with an ID.
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act: Call the service method to create a user.
        UserDto createdUserDto = userService.createUser(userDto);

        // Assert: Check that the returned DTO is not null and has the correct data.
        assertNotNull(createdUserDto);
        assertEquals(user.getId(), createdUserDto.getId());
        assertEquals(user.getFirstName(), createdUserDto.getFirstName());
        // Verify that the save method was called exactly once.
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Test Get User By ID - Success")
    @Test
    void testGetUserById_Success() {
        // Arrange: Mock the repository to find the user by ID.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act: Call the service method.
        UserDto foundUserDto = userService.getUserById(1L);

        // Assert: Check that the correct user DTO is returned.
        assertNotNull(foundUserDto);
        assertEquals(user.getId(), foundUserDto.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @DisplayName("Test Get User By ID - Not Found")
    @Test
    void testGetUserById_NotFound() {
        // Arrange: Mock the repository to return an empty Optional.
        long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert: Verify that a ResourceNotFoundException is thrown.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @DisplayName("Test Get All Users - Success with Users")
    @Test
    void testGetAllUsers_SuccessWithUsers() {
        // Arrange: Mock the repository to return a list of users.
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setFirstName("Jane");
        anotherUser.setLastName("Doe");
        anotherUser.setEmail("jane.doe@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user, anotherUser));

        // Act: Call the service method.
        List<UserDto> users = userService.getAllUsers();

        // Assert: Check that the list is correct.
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Jane", users.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @DisplayName("Test Get All Users - Success with No Users")
    @Test
    void testGetAllUsers_SuccessWithNoUsers() {
        // Arrange: Mock the repository to return an empty list.
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Call the service method.
        List<UserDto> users = userService.getAllUsers();

        // Assert: Check that the returned list is empty.
        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @DisplayName("Test Update User - Success")
    @Test
    void testUpdateUser_Success() {
        // Arrange: Create a DTO with updated information.
        UserDto updatedDto = new UserDto();
        updatedDto.setFirstName("Johnathan");
        updatedDto.setLastName("Doer");
        updatedDto.setEmail("johnathan.doer@example.com");

        // Mock the findById and save methods.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Call the update method.
        UserDto resultDto = userService.updateUser(1L, updatedDto);

        // Assert: Verify the returned DTO has the updated values.
        assertNotNull(resultDto);
        assertEquals(1L, resultDto.getId());
        assertEquals("Johnathan", resultDto.getFirstName());
        assertEquals("Doer", resultDto.getLastName());
        assertEquals("johnathan.doer@example.com", resultDto.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Test Update User - Not Found")
    @Test
    void testUpdateUser_NotFound() {
        // Arrange: Mock findById to return an empty Optional.
        long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert: Verify that a ResourceNotFoundException is thrown.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(userId, userDto);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class)); // Ensure save is never called.
    }

    @DisplayName("Test Delete User - Success")
    @Test
    void testDeleteUser_Success() {
        // Arrange: Mock the repository to confirm the user exists.
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        // Mock the void deleteById method.
        doNothing().when(userRepository).deleteById(userId);

        // Act: Call the delete method and assert no exception is thrown.
        assertDoesNotThrow(() -> userService.deleteUser(userId));

        // Assert: Verify that both existsById and deleteById were called.
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @DisplayName("Test Delete User - Not Found")
    @Test
    void testDeleteUser_NotFound() {
        // Arrange: Mock the repository to confirm the user does not exist.
        long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert: Verify that a ResourceNotFoundException is thrown.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong()); // Ensure delete is never called.
    }
}

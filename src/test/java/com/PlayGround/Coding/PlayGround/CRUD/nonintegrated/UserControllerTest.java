package com.PlayGround.Coding.PlayGround.CRUD.nonintegrated;

import com.PlayGround.Coding.PlayGround.CRUD.controller.UserController;
import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.exception.ResourceNotFoundException;
import com.PlayGround.Coding.PlayGround.CRUD.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
    }

    @DisplayName("POST /api/v1/users - Create User Success")
    @Test
    void testCreateUser_Success() throws Exception {
        // Arrange
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @DisplayName("GET /api/v1/users/{id} - Get User By ID Success")
    @Test
    void testGetUserById_Success() throws Exception {
        // Arrange
        long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(userService, times(1)).getUserById(userId);
    }

    @DisplayName("GET /api/v1/users/{id} - Get User By ID Not Found")
    @Test
    void testGetUserById_NotFound() throws Exception {
        // Arrange
        long userId = 99L;
        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userId);
    }

    @DisplayName("GET /api/v1/users - Get All Users Success")
    @Test
    void testGetAllUsers_Success() throws Exception {
        // Arrange
        UserDto anotherUserDto = new UserDto();
        anotherUserDto.setId(2L);
        anotherUserDto.setFirstName("Jane");
        anotherUserDto.setLastName("Doe");
        anotherUserDto.setEmail("jane.doe@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(userDto, anotherUserDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(userService, times(1)).getAllUsers();
    }

    @DisplayName("GET /api/v1/users - Get All Users Empty List")
    @Test
    void testGetAllUsers_Empty() throws Exception {
        // Arrange
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService, times(1)).getAllUsers();
    }

    @DisplayName("PUT /api/v1/users/{id} - Update User Success")
    @Test
    void testUpdateUser_Success() throws Exception {
        // Arrange
        long userId = 1L;
        UserDto updatedDto = new UserDto();
        updatedDto.setFirstName("Johnathan");
        updatedDto.setLastName("Doer");
        updatedDto.setEmail("john.doer@example.com");

        when(userService.updateUser(eq(userId), any(UserDto.class))).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnathan")))
                .andExpect(jsonPath("$.lastName", is("Doer")));

        verify(userService, times(1)).updateUser(eq(userId), any(UserDto.class));
    }

    @DisplayName("PUT /api/v1/users/{id} - Update User Not Found")
    @Test
    void testUpdateUser_NotFound() throws Exception {
        // Arrange
        long userId = 99L;
        when(userService.updateUser(eq(userId), any(UserDto.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(userId), any(UserDto.class));
    }

    @DisplayName("DELETE /api/v1/users/{id} - Delete User Success")
    @Test
    void testDeleteUser_Success() throws Exception {
        // Arrange
        long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));

        verify(userService, times(1)).deleteUser(userId);
    }

    @DisplayName("DELETE /api/v1/users/{id} - Delete User Not Found")
    @Test
    void testDeleteUser_NotFound() throws Exception {
        // Arrange
        long userId = 99L;
        doThrow(new ResourceNotFoundException("User not found with id: " + userId))
                .when(userService).deleteUser(userId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userId);
    }
}

package com.PlayGround.Coding.PlayGround.CRUD.integrated;

import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;
import com.PlayGround.Coding.PlayGround.CRUD.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the complete end-to-end CRUD flow of the User API.
 * This test loads the full Spring application context and interacts with the API endpoints.
 * It uses an ordered execution to test the lifecycle of a user resource:
 * 1. Create
 * 2. Read
 * 3. Update
 * 4. Read All
 * 5. Delete
 * 6. Verify Deletion
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ensures tests run in a specific order
@DisplayName("End-to-End CRUD Flow for User API")
class EndToEndFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    // A static variable to hold the ID of the user created in the first test.
    // This allows subsequent tests to use the same user for a complete flow.
    private static Long createdUserId;
    private static UserDto userDto;

    @BeforeAll
    static void beforeAll() {
        // Initialize the DTO that will be used across tests
        userDto = new UserDto();
        userDto.setFirstName("E2E");
        userDto.setLastName("Tester");
        userDto.setEmail("e2e.tester@example.com");
    }

    @AfterAll
    static void tearDown(@Autowired UserRepository userRepository) {
        // Clean up the created user if it still exists to ensure a clean state for other tests.
        // The existsById() check is not necessary as deleteById() is a no-op if the ID is not found.
        // This optimization saves one database query.
        if (createdUserId != null) {
            userRepository.deleteById(createdUserId);
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. POST /users - Create a new user")
    void testCreateUser() throws Exception {
        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andReturn();

        // Extract the ID from the response for the next tests
        String responseBody = result.getResponse().getContentAsString();
        UserDto createdUser = objectMapper.readValue(responseBody, UserDto.class);
        createdUserId = createdUser.getId();
        Assertions.assertNotNull(createdUserId);
    }

    @Test
    @Order(2)
    @DisplayName("2. GET /users/{id} - Retrieve the created user")
    void testGetUserById() throws Exception {
        Assertions.assertNotNull(createdUserId, "Create test must run first to set the user ID.");

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdUserId.intValue())))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())));
    }

    @Test
    @Order(3)
    @DisplayName("3. PUT /users/{id} - Update the user's details")
    void testUpdateUser() throws Exception {
        Assertions.assertNotNull(createdUserId, "Create test must run first to set the user ID.");

        // Arrange
        UserDto updatedDto = new UserDto();
        updatedDto.setFirstName("E2E_Updated");
        updatedDto.setLastName("Tester_Updated");
        updatedDto.setEmail("e2e.updated@example.com");

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdUserId.intValue())))
                .andExpect(jsonPath("$.firstName", is("E2E_Updated")))
                .andExpect(jsonPath("$.lastName", is("Tester_Updated")));
    }

    @Test
    @Order(4)
    @DisplayName("4. GET /users - Verify the updated user is in the list")
    void testGetAllUsers() throws Exception {
        Assertions.assertNotNull(createdUserId, "Create test must run first to set the user ID.");

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + createdUserId + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + createdUserId + ")].firstName", contains("E2E_Updated")));
    }

    @Test
    @Order(5)
    @DisplayName("5. DELETE /users/{id} - Delete the user")
    void testDeleteUser() throws Exception {
        Assertions.assertNotNull(createdUserId, "Create test must run first to set the user ID.");

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    @Test
    @Order(6)
    @DisplayName("6. GET /users/{id} - Verify user is not found after deletion")
    void testGetUserAfterDelete() throws Exception {
        Assertions.assertNotNull(createdUserId, "Create test must run first to set the user ID.");

        // Act & Assert: Check that the API returns 404 Not Found
        mockMvc.perform(get("/api/v1/users/{id}", createdUserId))
                .andExpect(status().isNotFound());

        // Final check: Verify directly against the repository that the user is gone
        assertFalse(userRepository.existsById(createdUserId), "User should not exist in the database after deletion.");
    }
}

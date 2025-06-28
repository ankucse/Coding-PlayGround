package com.PlayGround.Coding.PlayGround.CRUD.dto;

/**
 * Data Transfer Object (DTO) for representing user data.
 * This class is used to transfer user information between different layers of the application,
 * such as between the controller and service layers, and in API request/response bodies.
 */
public class UserDto {

    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The user's email address. This is expected to be unique for each user.
     */
    private String email;

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The new ID for the user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name for the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name for the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
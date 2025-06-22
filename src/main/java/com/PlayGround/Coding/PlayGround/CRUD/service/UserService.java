package com.PlayGround.Coding.PlayGround.CRUD.service;


import com.PlayGround.Coding.PlayGround.CRUD.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}

package ru.practicum.explorewithmeservice.user.service;

import ru.practicum.explorewithmeservice.user.dto.UserRequestDto;
import ru.practicum.explorewithmeservice.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto updateUser(
            Long userId,
            UserRequestDto userRequestDto
    );

    void deleteUser(
            Long userId
    );

    UserResponseDto createUser(
            UserRequestDto userRequestDto
    );

    UserResponseDto getUserById(
            Long userId
    );

    List<UserResponseDto> getAllUsers(
            Integer start,
            Integer size,
            List<Long> ids
    );

}
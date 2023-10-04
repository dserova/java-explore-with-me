package ru.practicum.explorewithmeservice.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.user.dto.UserRequestDto;
import ru.practicum.explorewithmeservice.user.dto.UserResponseDto;
import ru.practicum.explorewithmeservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private static final String userIdAlias = "userId";
    private static final String fromRequestParam = "from";
    private static final String sizeRequestParam = "size";
    private final UserService userService;

    @PostMapping(value = "/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(
            @Valid @RequestBody UserRequestDto request
    ) {
        return userService.createUser(
                request
        );
    }

    @PatchMapping("/admin/users/{" + userIdAlias + "}")
    public UserResponseDto updateUser(
            @PathVariable(name = userIdAlias) Long categoryId,
            @Valid @RequestBody UserRequestDto request
    ) {
        return userService.updateUser(
                categoryId,
                request
        );
    }

    @DeleteMapping("/admin/users/{" + userIdAlias + "}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable(name = userIdAlias) Long userId
    ) {
        userService.deleteUser(
                userId
        );
    }

    @GetMapping("/admin/users")
    public List<UserResponseDto> getAllUsers(
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size,
            @RequestParam(name = "ids", required = false) List<Long> ids
    ) {
        return userService.getAllUsers(
                from,
                size,
                ids
        );
    }

    @GetMapping("/admin/users/{" + userIdAlias + "}")
    public UserResponseDto getUserById(
            @PathVariable(name = userIdAlias) Long userId
    ) {
        return userService.getUserById(
                userId
        );
    }
}

package ru.practicum.explorewithmeservice.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmeservice.error.UserNotFoundException;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.helpers.Paging;
import ru.practicum.explorewithmeservice.user.dto.UserRequestDto;
import ru.practicum.explorewithmeservice.user.dto.UserResponseDto;
import ru.practicum.explorewithmeservice.user.model.User;
import ru.practicum.explorewithmeservice.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final Paging paging = new Paging();

    private final Helper helper = new Helper();

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto data) {
        Function<User, User> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    Optional.ofNullable(data.getName()).ifPresent(updating::setName);
                    return updating;
                }
        ).andThen(
                userRepository::save
        ).andThen(
                helper.to(UserResponseDto.class)
        ).apply(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("Check your parameters in the path."))
        );
    }

    @Override
    public UserResponseDto createUser(UserRequestDto data) {
        Function<UserRequestDto, UserRequestDto> chain = Function.identity();
        return chain.andThen(
                helper.to(User.class)
        ).andThen(
                userRepository::save
        ).andThen(
                helper.to(UserResponseDto.class)
        ).apply(
                data
        );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        Function<User, User> chain = Function.identity();
        return chain.andThen(
                helper.to(UserResponseDto.class)
        ).apply(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("Check your parameters in the path."))
        );
    }

    @Override
    public List<UserResponseDto> getAllUsers(Integer start, Integer size, List<Long> ids) {
        Function<Page<User>, Page<User>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(UserResponseDto.class)
        ).apply(
                userRepository.findAllById(ids, paging.getPageable(start, size))
                        .orElseThrow(() -> new UserNotFoundException("Check your parameters in the path."))
        );
    }

}



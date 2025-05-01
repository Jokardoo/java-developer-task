package com.example.test.service;

import com.example.test.domain.user.Role;
import com.example.test.domain.user.User;
import com.example.test.domain.user.UserEntity;
import com.example.test.exception.UserNotFoundException;
import com.example.test.mapper.UserToEntityMapper;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserToEntityMapper userToEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long id) {
        return userToEntityMapper.toModel(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found"))
        );
    }

    public User getByPhoneNumber(String phoneNumber) {
        return userToEntityMapper.toModel(userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UserNotFoundException("User not found"))
        );
    }

    public User create(User user) {
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new IllegalStateException("Phone number " + user.getPhoneNumber() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);

        UserEntity savedUserEntity = userRepository.save(userToEntityMapper.toEntity(user));
        return userToEntityMapper.toModel(savedUserEntity);
    }

    public User update(Long id, User user) {

        User foundUser = userToEntityMapper.toModel(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id = " + id + " not found!"))
        );

        if (user.getPassword() != null && !user.getPassword().isEmpty())
            foundUser.setPassword(passwordEncoder.encode(user.getPassword()));

        // Если номер телефона был введен
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            // Если номер телефона не занят
            if (!userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                foundUser.setPhoneNumber(user.getPhoneNumber());
            }
            else {
                throw new IllegalStateException("Phone number " + user.getPhoneNumber() + " already exists");
            }
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty())
            foundUser.setEmail(user.getEmail());

        if (user.getName() != null && !user.getName().isEmpty())
            foundUser.setName(user.getName());

        if (user.getBirthday() != null)
            foundUser.setBirthday(user.getBirthday());

        if (user.getPatronymic() != null && !user.getPatronymic().isEmpty())
            foundUser.setPatronymic(user.getPatronymic());



        return userToEntityMapper.
                toModel(userRepository
                        .save(userToEntityMapper
                                .toEntity(foundUser)
                        ));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}

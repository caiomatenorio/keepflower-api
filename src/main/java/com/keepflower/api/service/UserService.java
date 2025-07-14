package com.keepflower.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keepflower.api.exception.UsernameAlreadyInUseException;
import com.keepflower.api.model.User;
import com.keepflower.api.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    private boolean isUsernameInUse(String username) {
        return userRepository.existsByUsername(username);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void createUser(String username, String password) throws UsernameAlreadyInUseException {
        if (isUsernameInUse(username)) {
            throw new UsernameAlreadyInUseException();
        }

        String encodedPassword = encodePassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
    }
}

package com.keepflower.api.service;

import com.keepflower.api.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keepflower.api.exception.UsernameAlreadyInUseException;
import com.keepflower.api.model.User;
import com.keepflower.api.repository.UserRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isUsernameInUse(String username) {
        return userRepository.existsByUsername(username);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void createUser(String username, String password) throws UsernameAlreadyInUseException {
        if (isUsernameInUse(username)) {
            throw new UsernameAlreadyInUseException(username);
        }

        String encodedPassword = encodePassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean validateCredentials(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElse(false);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findByUsernameOrThrow(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}

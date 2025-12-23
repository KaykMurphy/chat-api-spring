package com.example.demo.auth;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginResponse login(String rawUsername){

        String normalizedUsername = rawUsername.trim().toLowerCase();

        User user = userRepository.findByUsername(normalizedUsername)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(normalizedUsername);
                    return userRepository.save(newUser);
                });

        return new LoginResponse(
                user.getId(),
                user.getUsername()
        );
    }

}

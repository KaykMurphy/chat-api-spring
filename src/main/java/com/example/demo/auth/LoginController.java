package com.example.demo.auth;

import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody  LoginRequest loginRequest){

        LoginResponse response = loginService.login(loginRequest.username());

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}

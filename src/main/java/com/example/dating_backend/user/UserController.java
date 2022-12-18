package com.example.dating_backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public UserDto.userSignUpRes signUp(final @RequestBody UserDto.userSignUpReq req){
        return userService.signUp(req);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(final @RequestBody UserDto.userSignInReq req){
        return userService.signIn(req);
    }
}

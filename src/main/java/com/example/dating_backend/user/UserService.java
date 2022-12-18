package com.example.dating_backend.user;

import com.example.dating_backend.jwt.JwtProvider;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserDto.userSignUpRes signUp(UserDto.userSignUpReq req) {
        if(userRepository.findUserByUserEmail(req.getEmail()).isPresent()){
            throw new DuplicateRequestException("이미 존재하는 이메일 입니다.");
        }

        User savedUser = userRepository.save(
                User.builder()
                        .userEmail(req.getEmail())
                        .userPassword(passwordEncoder.encode(req.getPassword()))
                        .build()
        );

        return new UserDto.userSignUpRes(savedUser.getUserEmail());
    }

    public ResponseEntity<?> signIn(UserDto.userSignInReq req) {
        User user = userRepository.findUserByUserEmail(req.getEmail()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 유저 정보입니다.")
        );
        if(!passwordEncoder.matches(req.getPassword(), user.getUserPassword())){ throw new RuntimeException("패스워드가 올바르지 않습니다.");}

        String jwtToken = jwtProvider.createToken(user);
        System.out.println(jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, jwtToken);

        return ResponseEntity.ok().headers(headers).body(new UserDto.userSignInRes(user));
    }
}

package com.example.jwt_demo.controller;

import com.example.jwt_demo.domain.Admin;
import com.example.jwt_demo.domain.Gender;
import com.example.jwt_demo.domain.User;
import com.example.jwt_demo.repository.UserRepository;
import com.example.jwt_demo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

//    final String BIRTH = "001200";
//    final String EMAIL = "aabbcc@gmail.com";
//    final String NICKNAME = "침착맨";
//    final Long SEQUENCEID = Long.valueOf(1);
//    final Gender GENDER = Gender.남;
//    final Admin ADMIN = Admin.일반회원;


    @PostMapping("/join")
    public String join(@RequestBody User userForm){

        User user = User.builder()
                .userEmail(userForm.getUserEmail())
                .userBirth(userForm.getUserBirth())
                .userNickname(userForm.getUserNickname())
                .admin(userForm.getAdmin())
                .gender(userForm.getGender())
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();
        log.info("로그인 시도됨");

        userRepository.save(user);


        return user.toString();

    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        log.info("user email = {}", user.get("email"));
        User member = userRepository.findByUserEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
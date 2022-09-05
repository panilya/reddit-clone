package com.panilya.redditserver.controller;

import com.panilya.redditserver.auth.JwtProvider;
import com.panilya.redditserver.dto.JwtTokenResponseDto;
import com.panilya.redditserver.dto.LoginDto;
import com.panilya.redditserver.dto.SignUpDto;
import com.panilya.redditserver.model.User;
import com.panilya.redditserver.repository.RoleRepository;
import com.panilya.redditserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtProvider jwtProvider, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);

        String jwtToken = jwtProvider.generateJwtToken(loginDTO.getUsername());
        return ResponseEntity.ok(new JwtTokenResponseDto("Bearer " + jwtToken));
    }

    @PostMapping(value = "/api/v1/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        User user = new User(signUpDto.getUsername(), passwordEncoder.encode(signUpDto.getPassword()), signUpDto.getEmail());
        user.setUserRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);

        return ResponseEntity.ok("Registration is successful");
    }

}

package com.panilya.redditserver.controller;

import com.panilya.redditserver.dto.UserDto;
import com.panilya.redditserver.model.User;
import com.panilya.redditserver.repository.UserRepository;
import com.panilya.redditserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MainController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MainController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("user/save")
    public void saveUser(@RequestBody UserDto userDto) {
        userService.registerNewUser(userDto);
    }

    @GetMapping("test")
    public String test() {
        return "Test";
    }

    @GetMapping("user/{id}")
    public String getUser(@PathVariable(name = "id") Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get().toString();
    }
}

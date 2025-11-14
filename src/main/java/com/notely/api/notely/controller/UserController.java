package com.notely.api.notely.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notely.api.notely.dto.AppUserDTO;
import com.notely.api.notely.service.classes.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllActiveUsers() {
        try {
            List<AppUserDTO> users = userService.getAllActiveUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AppUserDTO> getCurrentUser() {
        try {
            AppUserDTO currentUser = userService.getCurrentUser();
            return ResponseEntity.ok(currentUser);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


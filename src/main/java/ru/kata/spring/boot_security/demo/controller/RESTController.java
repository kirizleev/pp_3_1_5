package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RESTController {
    private final UserService userService;
    private final RegistrationService registrationService;

    public RESTController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping("/user")
    public User getCurrentuser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return (User) userDetails;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/users")
    public List<User> printUsers() {
            return userService.listUsers();
    }

    @PutMapping("/edit_{id}")
    public ResponseEntity<HttpStatus> editUser(@RequestBody User updatedUser) {
        registrationService.edit(updatedUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete_{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.delete(userService.getById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addUser(@RequestBody User newUser) {
        registrationService.register(newUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

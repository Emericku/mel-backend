package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.dtos.requests.UserRequest;
import fr.polytech.melusine.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }

}

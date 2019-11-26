package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.UserRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Page<UserResponse> getUsers(
            @PageableDefault(size = 20, page = 0, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return userService.getUsers(pageable);
    }

}

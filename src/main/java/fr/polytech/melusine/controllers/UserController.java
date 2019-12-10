package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.UserRegistrationRequest;
import fr.polytech.melusine.models.dtos.requests.UserUpdateRequest;
import fr.polytech.melusine.models.dtos.responses.UserResponse;
import fr.polytech.melusine.models.dtos.responses.UserSearchResponse;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@Validated
@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public User createUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return userService.createUser(userRegistrationRequest);
    }

    @GetMapping(path = "/all")
    public Page<UserResponse> getUsers(
            @PageableDefault(size = 20, page = 0, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return userService.getUsers(pageable);
    }

    @PatchMapping(path = "{userId}")
    public User creditUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.creditUser(userId, request);
    }

    @GetMapping
    public Page<UserSearchResponse> searchUser(
            @PageableDefault(size = 20, page = 0, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam @Size(min = 3, max = 50) String name) {
        return userService.searchUser(name, pageable);
    }

}

package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/accounts", produces = "application/json; charset=UTF-8")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.createAccount(accountRequest);
    }


    @PutMapping(path = "/{id}")
    public void updateAccount(@PathVariable String id, @RequestBody AccountRequest accountRequest) {
        accountService.updateAccount(id, accountRequest);
    }

}


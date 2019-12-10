package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.services.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account", produces = "application/json; charset=UTF-8")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/{clientId}")
    public Account updateAccount(@PathVariable String clientId, @RequestBody AccountRequest accountRequest) {
        return accountService.updateAccount(clientId, accountRequest);
    }

}

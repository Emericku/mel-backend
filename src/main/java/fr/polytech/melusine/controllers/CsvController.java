package fr.polytech.melusine.controllers;

import fr.polytech.melusine.services.CsvService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/csv", produces = "application/json; charset=UTF-8")
public class CsvController {

    private CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void importUsers(@RequestParam("file") MultipartFile csv, HttpServletResponse response) throws Exception {
        csvService.importUsers(csv, response);
    }

    @GetMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportUsers(HttpServletResponse response) throws Exception {
        csvService.exportUsers(response);
    }

}

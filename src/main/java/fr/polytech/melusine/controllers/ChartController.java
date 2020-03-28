package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.ChartRequest;
import fr.polytech.melusine.models.dtos.responses.ChartResponse;
import fr.polytech.melusine.services.ChartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/charts", produces = "application/json; charset=UTF-8")
public class ChartController {

    private ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ChartResponse findChart(@RequestBody @Valid ChartRequest chartRequest) {
        return chartService.findChart(chartRequest);
    }

}

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

    @PostMapping(path = "/revenues")
    @ResponseStatus(HttpStatus.OK)
    public ChartResponse findRevenuesChart(@RequestBody @Valid ChartRequest chartRequest) {
        return chartService.findRevenuesChart(chartRequest);
    }

    @PostMapping(path = "/consumptions")
    @ResponseStatus(HttpStatus.OK)
    public ChartResponse findConsumptionsChart(@RequestBody @Valid ChartRequest chartRequest) {
        return chartService.findConsumptionsChart(chartRequest);
    }

}

package hei.school.prog3rattrapage.controller;

import hei.school.prog3rattrapage.Dto.TopDriverDto;
import hei.school.prog3rattrapage.service.RevenueService;
import hei.school.prog3rattrapage.model.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
public class StatisticsController {

    private final RevenueService revenueService;

    public StatisticsController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/statistics/top-earning-driver")
    public TopDriverDto getTopDriver(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        Driver driver = revenueService.findTopEarningDriver(from, to);

        if (driver == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No driver found"
            );
        }

        long revenue = revenueService.computeDriverRevenue(
                driver.getId(), from, to);

        return new TopDriverDto(
                driver.getId(),
                driver.getName(),
                revenue
        );
    }
}
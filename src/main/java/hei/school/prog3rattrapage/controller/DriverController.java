package hei.school.prog3rattrapage.controller;

import hei.school.prog3rattrapage.repository.DataRetriever;
import hei.school.prog3rattrapage.Dto.DriverStatisticsDto;
import hei.school.prog3rattrapage.Dto.RevenueDto;
import hei.school.prog3rattrapage.service.RevenueService;
import hei.school.prog3rattrapage.model.Driver;
import hei.school.prog3rattrapage.model.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DriverController {

    private final RevenueService revenueService;
    private final DataRetriever dataRetriever;

    public DriverController(RevenueService revenueService,
                            DataRetriever dataRetriever) {
        this.revenueService = revenueService;
        this.dataRetriever = dataRetriever;
    }

    @GetMapping("/drivers/{driverId}/revenue")
    public RevenueDto getRevenue(
            @PathVariable String driverId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        Driver driver = dataRetriever.findDriverById(driverId);

        if (driver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new RevenueDto(
                driver.getId(),
                driver.getName(),
                from,
                to,
                revenueService.computeDriverRevenue(driverId, from, to),
                revenueService.computeCooperativeFee(driverId, from, to)
        );
    }

    @GetMapping("/drivers/{driverId}/statistics")
    public DriverStatisticsDto getStatistics(@PathVariable String driverId) {

        Driver driver = dataRetriever.findDriverById(driverId);

        if (driver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Trip> trips = dataRetriever.findTripsByDriver(driverId);

        int completed = 0;
        int distance = 0;

        for (Trip t : trips) {
            if (t.isBillable()) {
                completed++;
                distance += t.getDistanceKm();
            }
        }

        return new DriverStatisticsDto(
                driver.getId(),
                driver.getName(),
                completed,
                distance,
                revenueService.computeAveragePricePerKm(driverId)
        );
    }
}
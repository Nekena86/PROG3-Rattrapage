package hei.school.prog3rattrapage.controller;

import hei.school.prog3rattrapage.repository.DataRetriever;
import hei.school.prog3rattrapage.Dto.DriverDto;
import hei.school.prog3rattrapage.Dto.VehicleDto;
import hei.school.prog3rattrapage.Dto.TripResponse;
import hei.school.prog3rattrapage.model.TripStatusRequest;
import hei.school.prog3rattrapage.model.Trip;
import hei.school.prog3rattrapage.model.TripStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TripController {

    private final DataRetriever dataRetriever;

    public TripController(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    @GetMapping("/trips")
    public List<TripResponse> getTrips() {

        List<TripResponse> result = new ArrayList<>();

        for (Trip t : dataRetriever.findAllTrips()) {
            result.add(toDto(t));
        }

        return result;
    }

    @PutMapping("/trips/{tripId}")
    public ResponseEntity<TripResponse> saveTrip(
            @PathVariable String tripId,
            @RequestBody Trip trip) {

        trip.setId(tripId);

        if (trip.getStatus() == null) {
            trip.setStatus(TripStatus.COMPLETED);
        }

        boolean created = dataRetriever.findTripById(tripId) == null;

        Trip saved = dataRetriever.saveTrip(trip);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
        }

        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/trips/{tripId}/status")
    public TripResponse updateStatus(
            @PathVariable String tripId,
            @RequestBody TripStatusRequest request) {

        Trip trip = dataRetriever.updateTripStatus(tripId, request.getStatus());

        return toDto(trip);
    }

    private TripResponse toDto(Trip trip) {
        return new TripResponse(
                trip.getId(),
                new DriverDto(trip.getDriver().getId(), trip.getDriver().getName()),
                new VehicleDto(trip.getVehicle().getId(), trip.getVehicle().getPlateNumber()),
                trip.getTripDate(),
                trip.getDepartureCity(),
                trip.getArrivalCity(),
                trip.getDistanceKm(),
                trip.getBilledAmount(),
                trip.getStatus()
        );
    }
}
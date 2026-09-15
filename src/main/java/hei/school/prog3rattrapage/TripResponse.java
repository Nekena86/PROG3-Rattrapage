package hei.school.prog3rattrapage;

import java.time.LocalDate;

public record TripResponse(
        String id,
        DriverDto driver,
        VehicleDto vehicle,
        LocalDate tripDate,
        String departureCity,
        String arrivalCity,
        int distanceKm,
        long billedAmount,
        TripStatus status
){}

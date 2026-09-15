package hei.school.prog3rattrapage;

public record DriverStatisticsDto(
        String driverId,
        String driverName,
        int completedTrips,
        int totalDistanceKm,
        double averagePricePerKm
) {}

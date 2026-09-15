package hei.school.prog3rattrapage.Dto;

public record DriverStatisticsDto(
        String driverId,
        String driverName,
        int completedTrips,
        int totalDistanceKm,
        double averagePricePerKm
) {}

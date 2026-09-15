package hei.school.prog3rattrapage.Dto;

import java.time.LocalDate;

public record RevenueDto(
        String driverId,
        String driverName,
        LocalDate from,
        LocalDate to,
        long revenue,
        long cooperativeFee
) {}

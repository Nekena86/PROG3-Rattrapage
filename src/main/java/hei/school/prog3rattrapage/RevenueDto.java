package hei.school.prog3rattrapage;

import java.time.LocalDate;

public record RevenueDto(
        String driverId,
        String driverName,
        LocalDate from,
        LocalDate to,
        long revenue,
        long cooperativeFee
) {}

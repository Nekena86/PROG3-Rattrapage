package hei.school.prog3rattrapage;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueService {
    private static final double COOPERATIVE_RATE = 0.15;
    private final DataRetriever dataRetriever;

    public RevenueService(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }


    public long computeDriverRevenue(String driverId,
                                     LocalDate from,
                                     LocalDate to) {
        long total = 0;

        List<Trip> trips = dataRetriever.findTripsByDriver(driverId);

        for (Trip t : trips) {
            if (t.isBillable()
                    && !t.getTripDate().isBefore(from)
                    && !t.getTripDate().isAfter(to)) {
                total += t.getBilledAmount();
            }
        }

        return total;
    }

    public long computeCooperativeFee(String driverId,
                                      LocalDate from,
                                      LocalDate to) {
        return Math.round(
                computeDriverRevenue(driverId, from, to) * COOPERATIVE_RATE
        );
    }

    public double computeAveragePricePerKm(String driverId) {
        long amount = 0;
        int km = 0;

        for (Trip t : dataRetriever.findTripsByDriver(driverId)) {
            if (t.isBillable()) {
                amount += t.getBilledAmount();
                km += t.getDistanceKm();
            }
        }

        return km == 0 ? 0 : (double) amount / km;
    }

    public Driver findTopEarningDriver(LocalDate from, LocalDate to) {
        Driver best = null;
        long max = 0;

        for (Driver d : dataRetriever.findAllDrivers()) {
            long revenue = computeDriverRevenue(d.getId(), from, to);

            if (revenue > max) {
                max = revenue;
                best = d;
            }
        }

        return best;
    }
}
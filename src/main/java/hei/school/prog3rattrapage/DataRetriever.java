package hei.school.prog3rattrapage;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRetriever {

    private final DbConnection dbConnection;

    public DataRetriever(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    public Driver findDriverById(String id) {

        String sql = "SELECT * FROM driver WHERE id=?";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapDriver(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Driver> findAllDrivers() {

        List<Driver> drivers = new ArrayList<>();

        String sql = "SELECT * FROM driver";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                drivers.add(mapDriver(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return drivers;
    }


    public Vehicle findVehicleById(String id) {

        String sql = "SELECT * FROM vehicle WHERE id=?";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapVehicle(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public Trip findTripById(String id) {

        String sql = "SELECT * FROM trip WHERE id=?";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapTrip(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Trip> findTripsByDriver(String driverId) {

        List<Trip> trips = new ArrayList<>();

        String sql = "SELECT * FROM trip WHERE driver_id=?";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setString(1, driverId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                trips.add(mapTrip(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trips;
    }

    public List<Trip> findTripsByPeriod(LocalDate from, LocalDate to) {

        List<Trip> trips = new ArrayList<>();

        String sql = """
                SELECT * FROM trip
                WHERE trip_date BETWEEN ? AND ?
                """;

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                trips.add(mapTrip(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trips;
    }

    public List<Trip> findAllTrips() {

        List<Trip> trips = new ArrayList<>();

        String sql = "SELECT * FROM trip";

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                trips.add(mapTrip(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trips;
    }


    public Trip saveTrip(Trip trip) {

        Trip existing = findTripById(trip.getId());

        if (existing == null) {

            String sql = """
                    INSERT INTO trip(
                    id,
                    driver_id,
                    vehicle_id,
                    trip_date,
                    departure_city,
                    arrival_city,
                    distance_km,
                    billed_amount,
                    status)
                    VALUES(?,?,?,?,?,?,?,?,?)
                    """;

            try (
                    Connection c = dbConnection.getConnection();
                    PreparedStatement ps = c.prepareStatement(sql)
            ) {

                ps.setString(1, trip.getId());
                ps.setString(2, trip.getDriver().getId());
                ps.setString(3, trip.getVehicle().getId());
                ps.setDate(4, Date.valueOf(trip.getTripDate()));
                ps.setString(5, trip.getDepartureCity());
                ps.setString(6, trip.getArrivalCity());
                ps.setInt(7, trip.getDistanceKm());
                ps.setLong(8, trip.getBilledAmount());
                ps.setString(9, trip.getStatus().name());

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            String sql = """
                    UPDATE trip SET
                    driver_id=?,
                    vehicle_id=?,
                    trip_date=?,
                    departure_city=?,
                    arrival_city=?,
                    distance_km=?,
                    billed_amount=?,
                    status=?
                    WHERE id=?
                    """;

            try (
                    Connection c = dbConnection.getConnection();
                    PreparedStatement ps = c.prepareStatement(sql)
            ) {

                ps.setString(1, trip.getDriver().getId());
                ps.setString(2, trip.getVehicle().getId());
                ps.setDate(3, Date.valueOf(trip.getTripDate()));
                ps.setString(4, trip.getDepartureCity());
                ps.setString(5, trip.getArrivalCity());
                ps.setInt(6, trip.getDistanceKm());
                ps.setLong(7, trip.getBilledAmount());
                ps.setString(8, trip.getStatus().name());
                ps.setString(9, trip.getId());

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return findTripById(trip.getId());
    }

    public Trip updateTripStatus(String tripId, TripStatus status) {

        String sql = """
                UPDATE trip
                SET status=?
                WHERE id=?
                """;

        try (
                Connection c = dbConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {

            ps.setString(1, status.name());
            ps.setString(2, tripId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return findTripById(tripId);
    }


    private Driver mapDriver(ResultSet rs) throws SQLException {

        Driver driver = new Driver();

        driver.setId(rs.getString("id"));
        driver.setName(rs.getString("name"));
        driver.setLicenseCategory(
                LicenseCategory.valueOf(rs.getString("license_category"))
        );
        driver.setAffiliationDate(
                rs.getDate("affiliation_date").toLocalDate()
        );

        return driver;
    }

    private Vehicle mapVehicle(ResultSet rs) throws SQLException {

        Vehicle vehicle = new Vehicle();

        vehicle.setId(rs.getString("id"));
        vehicle.setPlateNumber(rs.getString("plate_number"));
        vehicle.setType(
                VehicleType.valueOf(rs.getString("type"))
        );
        vehicle.setCapacityTons(rs.getDouble("capacity_tons"));

        return vehicle;
    }

    private Trip mapTrip(ResultSet rs) throws SQLException {

        Trip trip = new Trip();

        trip.setId(rs.getString("id"));
        trip.setDriver(findDriverById(rs.getString("driver_id")));
        trip.setVehicle(findVehicleById(rs.getString("vehicle_id")));
        trip.setTripDate(
                rs.getDate("trip_date").toLocalDate()
        );
        trip.setDepartureCity(rs.getString("departure_city"));
        trip.setArrivalCity(rs.getString("arrival_city"));
        trip.setDistanceKm(rs.getInt("distance_km"));
        trip.setBilledAmount(rs.getLong("billed_amount"));
        trip.setStatus(
                TripStatus.valueOf(rs.getString("status"))
        );

        return trip;
    }
}
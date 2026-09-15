package hei.school.prog3rattrapage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String id;
    private String plateNumber;
    private VehicleType type;
    private double capacityTons;
}

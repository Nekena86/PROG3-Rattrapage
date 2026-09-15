package hei.school.prog3rattrapage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    private String id;
    private String name;
    private LicenseCategory licenseCategory;
    private LocalDate affiliationDate;
}

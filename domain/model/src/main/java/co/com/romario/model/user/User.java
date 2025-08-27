package co.com.romario.model.user;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String userId;
    private String names;
    private String lastName;
    private LocalDate birthDate;
    private String documentType;
    private String documentNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
    private String occupation;
    private String company;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public boolean isValidAge() {
        return birthDate.isBefore((LocalDate.now().minusYears(18)));
    }

    public String getFullName() {
        return names + " " + lastName;
    }
}

package co.com.romario.model.user;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private UUID id;
    private String names;
    private String lastName;
    private LocalDate birthDate;
    private String documentType;
    private String documentNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
}

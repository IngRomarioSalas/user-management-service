package co.com.romario.r2dbc.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("usuarios")
public class UserEntity {

    @Id
    private UUID id;
    
    @Column("nombres")
    private String names;
    
    @Column("apellidos")
    private String lastName;
    
    @Column("fecha_nacimiento")
    private LocalDate birthDate;
    
    @Column("tipo_documento")
    private String documentType;
    
    @Column("numero_documento")
    private String documentNumber;
    
    @Column("direccion")
    private String address;
    
    @Column("telefono")
    private String phoneNumber;
    
    @Column("correo_electronico")
    private String email;
    
    @Column("salario_base")
    private Double baseSalary;
    
}

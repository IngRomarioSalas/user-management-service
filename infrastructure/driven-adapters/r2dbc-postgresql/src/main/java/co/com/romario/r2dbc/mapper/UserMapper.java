package co.com.romario.r2dbc.mapper;
import org.springframework.stereotype.Component;

import co.com.romario.model.user.User;
import co.com.romario.r2dbc.entity.UserEntity;

@Component
public class UserMapper {

    public UserEntity toEntity(User model) {
        if (model == null) return new UserEntity();
        return UserEntity.builder()
                .id(model.getId())
                .names(model.getNames())
                .lastName(model.getLastName())
                .birthDate(model.getBirthDate())
                .documentType(model.getDocumentType())
                .documentNumber(model.getDocumentNumber())
                .address(model.getAddress())
                .phoneNumber(model.getPhoneNumber())
                .email(model.getEmail())
                .baseSalary(model.getBaseSalary())
                .build();
    }

    public User toModel(UserEntity entity) {
        if (entity == null) return User.builder().build();
        return User.builder()
                .id(entity.getId())
                .names(entity.getNames())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .baseSalary(entity.getBaseSalary())
                .build();
    }
}

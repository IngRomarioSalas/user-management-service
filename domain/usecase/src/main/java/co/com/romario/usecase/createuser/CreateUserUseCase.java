package co.com.romario.usecase.createuser;

import java.time.LocalDateTime;
import java.util.UUID;

import co.com.romario.model.user.User;
import co.com.romario.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    
    public Mono<User> registerUser(User user) {
        return validateUserData(user)
                .then(checkUserDoesNotExist(user))
                .then(createUser(user));
    }
    
    private Mono<Void> validateUserData(User user) {
        if (!user.isValidAge()) {
            return Mono.error(new RuntimeException("El usuario debe ser mayor de edad"));
        }
        
        if (user.getBaseSalary() == null || user.getBaseSalary() < 0) {
            return Mono.error(new RuntimeException("El salario base debe ser mayor a 0"));
        }
        
        if (user.getBaseSalary() > 15000000) {
            return Mono.error(new RuntimeException("El salario base no puede exceder 15,000,000"));
        }

        if( user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return Mono.error(new RuntimeException("El correo electrónico no es válido"));
        }
        
        return Mono.empty();
    }
    
    private Mono<Void> checkUserDoesNotExist(User user) {
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new RuntimeException("Correo electrónico ya registrado"));
                    }

                    return userRepository.existsByDocumentNumber(user.getDocumentNumber())
                            .flatMap(docExists -> {
                                if (docExists) {
                                    return Mono
                                            .error(new RuntimeException("Número de documento ya registrado"));
                                }
                                return Mono.empty();
                            });
                });
    }
    
    private Mono<User> createUser(User user) {
        User newUser = user.toBuilder()
                .userId(UUID.randomUUID().toString())
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        return userRepository.save(newUser);
    }
    
}

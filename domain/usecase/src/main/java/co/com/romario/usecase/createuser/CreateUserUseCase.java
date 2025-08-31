package co.com.romario.usecase.createuser;

import co.com.romario.model.user.User;
import co.com.romario.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public Mono<User> registerUser(User user) {
        if (user.getNames() == null || user.getNames().isBlank())
            return Mono.error(new IllegalArgumentException("nombres es obligatorio"));
        if (user.getLastName() == null || user.getLastName().isBlank())
            return Mono.error(new IllegalArgumentException("apellidos es obligatorio"));
        if (user.getEmail() == null || user.getEmail().isBlank())
            return Mono.error(new IllegalArgumentException("correo es obligatorio"));
        if (user.getBaseSalary() == null || user.getBaseSalary() < 0 || user.getBaseSalary() > 15_000_000)
            return Mono.error(new IllegalArgumentException("salario inválido"));
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return Mono.error(new IllegalArgumentException("El correo electrónico no es válido"));
        }

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalStateException("correo ya registrado"));
                    }
                    return userRepository.save(user.toBuilder().build());
                });
    }

}

package co.com.romario.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.romario.model.user.User;
import co.com.romario.usecase.createuser.CreateUserUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
//private  final UseCase2 useCase2;

    private final CreateUserUseCase createUserUseCase;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(user -> {
                    log.info("Iniciando creación de usuario: {}", user.getEmail());
                    return createUserUseCase.registerUser(user)
                            .doOnSuccess(u -> log.info("Usuario creado exitosamente: {}", u.getUserId()))
                            .doOnError(e -> log.error("Error al crear usuario: {}", e.getMessage()));
                })
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(error ->
                        ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
    
}

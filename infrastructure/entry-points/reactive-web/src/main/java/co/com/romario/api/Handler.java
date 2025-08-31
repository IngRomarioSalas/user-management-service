package co.com.romario.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.romario.model.user.User;
import co.com.romario.usecase.createuser.CreateUserUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CreateUserUseCase createUserUseCase;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(User.class)
            .flatMap(createUserUseCase::registerUser)
            .flatMap(savedUser -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(savedUser));
    }

}

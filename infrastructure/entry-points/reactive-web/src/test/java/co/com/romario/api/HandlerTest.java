package co.com.romario.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.romario.model.user.User;
import co.com.romario.usecase.createuser.CreateUserUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HandlerTest {

     private CreateUserUseCase createUserUseCase;
    private Handler handler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        createUserUseCase = mock(CreateUserUseCase.class);
        handler = new Handler(createUserUseCase);

        // Router que conecta POST /users al handler
        RouterFunction<ServerResponse> router = RouterFunctions.route()
                .POST("/users", handler::registerUser)
                .build();

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void registerUser_success() {
        User inputUser = new User();
        inputUser.setNames("Alice");
        User savedUser = new User();
        savedUser.setNames("AliceSaved");

        when(createUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.just(savedUser));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inputUser)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .value(body -> body.contains("AliceSaved"));

        verify(createUserUseCase).registerUser(any(User.class));
    }

    @Test
    void registerUser_illegalArgument() {
        User inputUser = new User();

        when(createUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid user")));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inputUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(body -> body.contains("Invalid user"));
    }

    @Test
    void registerUser_genericError() {
        User inputUser = new User();

        when(createUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inputUser)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .value(body -> body.contains("Unexpected error"));
    }
}

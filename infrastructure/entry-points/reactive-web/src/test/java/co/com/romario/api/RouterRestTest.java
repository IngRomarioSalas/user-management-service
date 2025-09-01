package co.com.romario.api;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.romario.model.user.User;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = { RouterRest.class, Handler.class })
@WebFluxTest
class RouterRestTest {
    @MockBean
    private Handler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterRest routerRest = new RouterRest();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(handler)).build();
    }

    @Test
    void createUser_ok() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .names("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.of(1995, 4, 12))
                .email("juan.perez@example.com")
                .baseSalary(2_500_000.0)
                .build();

        Mockito.when(handler.registerUser(any()))
                .thenReturn(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.names").isEqualTo("Juan")
                .jsonPath("$.email").isEqualTo("juan.perez@example.com");
    }

    // @Test
    // void createUser_error() {
    //     webTestClient.post().uri("/api/v1/usuarios")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .bodyValue("{\"names\": \"\", \"lastName\": \"\", \"email\": \"\"}")
    //             .exchange() 
    //             .expectStatus().isBadRequest()
    //             .expectBody()
    //             .jsonPath("$.error").isEqualTo("nombres es obligatorio");
    // }
}

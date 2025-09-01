package co.com.romario.api.config;

import co.com.romario.api.Handler;
import co.com.romario.api.RouterRest;
import co.com.romario.usecase.createuser.CreateUserUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@ContextConfiguration(classes = ConfigTest.DummyRouterConfig.class)
@AutoConfigureWebTestClient
class ConfigTest {

     private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<?> route = new DummyRouterConfig().dummyRouter();
        this.webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/dummy")
                .exchange()
                .expectStatus().isOk();
                // ... el resto de tus asserts
    }

    static class DummyRouterConfig {
        public RouterFunction<?> dummyRouter() {
            return RouterFunctions.route()
                    .GET("/dummy", request -> ServerResponse.ok().build())
                    .build();
        }
    }
}



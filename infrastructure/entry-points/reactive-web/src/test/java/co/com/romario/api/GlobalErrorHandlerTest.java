package co.com.romario.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalErrorHandlerTest {

    private GlobalErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new GlobalErrorHandler();
    }

    private String getResponseBody(MockServerWebExchange exchange) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        return exchange.getResponse().getBody()
                .next()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .block();
    }

    @Test
    void handle_whenIllegalArgumentException_thenBadRequest() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        Mono<Void> result = errorHandler.handle(exchange, new IllegalArgumentException("Invalid param"));

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(getResponseBody(exchange)).contains("Invalid param");
    }

    @Test
    void handle_whenIllegalStateException_thenConflict() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        Mono<Void> result = errorHandler.handle(exchange, new IllegalStateException("Illegal state"));

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(getResponseBody(exchange)).contains("Illegal state");
    }

    @Test
    void handle_whenResponseStatusException_thenCustomStatus() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
        Mono<Void> result = errorHandler.handle(exchange, ex);

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponseBody(exchange)).contains("Not found!");
    }

    @Test
    void handle_whenGenericException_thenInternalServerError() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        Mono<Void> result = errorHandler.handle(exchange, new RuntimeException("Something went wrong"));

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(getResponseBody(exchange)).contains("Error interno en el servidor");
    }
}

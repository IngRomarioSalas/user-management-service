package co.com.romario.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class SecurityHeadersConfigTest {

    @Test
    void filter_shouldSetSecurityHeaders() {
        // Crear mocks
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        MockServerHttpResponse response = new MockServerHttpResponse();
        when(exchange.getResponse()).thenReturn(response);
        
        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Ejecutar el filtro
        SecurityHeadersConfig filter = new SecurityHeadersConfig();
        Mono<Void> result = filter.filter(exchange, chain);

        // Verificar que el chain fue llamado
        StepVerifier.create(result)
                .verifyComplete();

        HttpHeaders headers = exchange.getResponse().getHeaders();
        assert headers.getFirst("Content-Security-Policy")
                .equals("default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        assert headers.getFirst("Strict-Transport-Security").equals("max-age=31536000;");
        assert headers.getFirst("X-Content-Type-Options").equals("nosniff");
        assert headers.getFirst("Server").equals("");
        assert headers.getFirst("Cache-Control").equals("no-store");
        assert headers.getFirst("Pragma").equals("no-cache");
        assert headers.getFirst("Referrer-Policy").equals("strict-origin-when-cross-origin");

        // Verificar que el chain.filter fue invocado
        verify(chain, times(1)).filter(exchange);
    }
}


package co.com.romario.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
@Tag(name = "User API", description = "Operaciones sobre usuarios")
public class RouterRest {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/usecase/path",
            produces = { "application/json" },
            method = { RequestMethod.GET },
            beanClass = Handler.class,
            beanMethod = "listenGETUseCase",
            operation = @Operation(summary = "Obtener información de usecase", description = "Devuelve datos de prueba")
        ),
        @RouterOperation(
            path = "/api/usecase/otherpath",
            produces = { "application/json" },
            method = { RequestMethod.POST },
            beanClass = Handler.class,
            beanMethod = "listenPOSTUseCase",
            operation = @Operation(summary = "Crear información en otherpath")
        ),
        @RouterOperation(
            path = "/api/otherusercase/path",
            produces = { "application/json" },
            method = { RequestMethod.GET },
            beanClass = Handler.class,
            beanMethod = "listenGETOtherUseCase",
            operation = @Operation(summary = "Obtener datos de otro caso de uso")
        ),
        @RouterOperation(
            path = "/api/v1/usuarios",
            produces = { "application/json" },
            method = { RequestMethod.POST },
            beanClass = Handler.class,
            beanMethod = "createUser",
            operation = @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/usecase/otherpath"), handler::listenPOSTUseCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase)
                .andRoute(POST("/api/v1/usuarios"), handler::createUser));
    }
}

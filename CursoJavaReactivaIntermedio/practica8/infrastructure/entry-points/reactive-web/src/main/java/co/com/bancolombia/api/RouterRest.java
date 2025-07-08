package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/box/create"), handler::createBox)
                .andRoute(PUT("/api/box/open/{id}"), handler::openBox)
                .andRoute(PUT("/api/box/close/{id}"), handler::closeBox)
                .andRoute(GET("/api/box/{id}"), handler::findBoxById)
                .andRoute(GET("/api/boxes"), handler::listAllBoxes)
                .andRoute(PUT("/api/box/update-name/{id}"), handler::updateBox)
                .andRoute(DELETE("/api/box/{id}"), handler::deleteBox)
                .and(route(PUT("/api/box/reopen/{id}"), handler::reopenBox));
    }
}

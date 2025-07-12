package co.com.bancolombia.api.config;

import co.com.bancolombia.api.exception.GlobalErrorHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ErrorHandlerConfig {

    @Bean
    public GlobalErrorHandler globalErrorHandler(ErrorAttributes errorAttributes,
                                                 WebProperties webProperties,
                                                 ApplicationContext applicationContext,
                                                 ServerCodecConfigurer codecConfigurer) {
        return new GlobalErrorHandler(
                errorAttributes,
                webProperties.getResources(),
                applicationContext,
                codecConfigurer
        );
    }
}


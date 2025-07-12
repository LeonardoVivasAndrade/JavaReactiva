package co.com.bancolombia.usecase.uploadmovements;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface RenderFile {
    Flux<Map<String, String>> render(byte[] bytes);
}

package co.com.bancolombia.usecase.getboxbyid;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetBoxByIdUseCase {
    private final BoxRepository repository;

    public Mono<Box> getBoxById(String id) {
        return repository.findById(id);
    }
}

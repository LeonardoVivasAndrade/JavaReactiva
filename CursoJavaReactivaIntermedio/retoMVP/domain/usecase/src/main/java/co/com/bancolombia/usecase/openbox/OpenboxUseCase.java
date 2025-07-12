package co.com.bancolombia.usecase.openbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OpenboxUseCase {
    private final BoxRepository repository;

    public Mono<Box> openBox(String boxId, BigDecimal openingAmount) {
        return repository.findById(boxId)
                .flatMap(box -> {
                    box.open(openingAmount);
                    return repository.save(box);
                });
    }
}

package co.com.bancolombia.usecase.closebox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CloseboxUseCase {
    private final BoxRepository repository;

    public Mono<Box> closeBox(String boxId, BigDecimal closingAmount) {
        return repository.findById(boxId)
                .flatMap(box -> {
                    box.close(closingAmount);
                    return repository.save(box);
                });
    }
}

package co.com.bancolombia.usecase.listallboxes;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ListAllBoxesUseCase {
    private final BoxRepository repository;

    public Flux<Box> listAllBoxes(BoxStatus status, LocalDateTime openedAfter, String createdBy) {
        return repository.findAll()
                .filter(box -> status == null || box.getStatus().equals(status))
                .filter(box -> box.getOpenedAt() != null && (openedAfter == null || box.getOpenedAt().isAfter(openedAfter)))
                .filter(box -> createdBy == null || box.getCreatedBy().equalsIgnoreCase(createdBy.trim()));
    }
}


package co.com.bancolombia.usecase.uploadmovements;

import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.MovementUploadReport;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UploadMovementsUseCase {
    private final MovementRepository repository;
    private final BoxRepository boxRepository;
    private final RenderFile renderFile;
    private final EventsGateway gateway;
    private static final Logger log = Logger.getLogger(UploadMovementsUseCase.class.getName());
    private Set<String> movementsId;

    public Mono<MovementUploadReport> uploadCSV(Flux<ByteBuffer> byteBuffer, MovementUploadReport report) {
        class ResultAccumulator {
            int success = 0;
            int failed = 0;
        }
        ResultAccumulator accumulator = new ResultAccumulator();
        movementsId = new HashSet<>();

        return boxRepository.findById(report.getBoxId())
                .flatMapMany(box -> byteBuffer.map(ByteBuffer::array)
                        .flatMapSequential(renderFile::render)
                        .flatMap(fila -> validateAndSave(report.getBoxId(), fila)
                                .doOnNext(movement -> accumulator.success++)
                                .onErrorResume(e -> {
                                    log.log(Level.WARNING, "Error: {0}", e.getMessage());
                                    accumulator.failed++;
                                    return Mono.empty();
                                })
                        )
                )
                .switchIfEmpty(Mono.error(new NoSuchElementException("BoxId " + report.getBoxId() + ", does not exist.")))
                .collectList()
                .map(movements -> buildReport(report, accumulator.success, accumulator.failed))
                .onErrorResume(e -> Mono.just(buildReport(report, accumulator.success, accumulator.failed)))
                .flatMap(movementUploadReport -> gateway.boxMovementsUploadedEvent(movementUploadReport)
                        .then(Mono.just(movementUploadReport)));
    }

    private Mono<Movement> validateAndSave(String boxId, Map<String, String> fila) {
        try {
            if (movementsId.contains(fila.get("movementId").trim()))
                throw new IllegalArgumentException("Movimiento[" + fila.get("movementId").trim() + "] : El campo 'movementId' está duplicado.");
            movementsId.add(fila.get("movementId").trim());

            return repository.save(
                    Movement.builder()
                            .movementId(fila.get("movementId").trim())
                            .boxIdSrt(fila.get("boxId").trim(), boxId)
                            .type(fila.get("type").trim())
                            .amountStr(fila.get("amount").trim())
                            .currency(fila.get("currency").trim())
                            .description(fila.get("description").trim())
                            .dateIso(fila.get("date").trim())
                            .build()
            );
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private MovementUploadReport buildReport(MovementUploadReport report, int success, int failed) {
        report.setTotal(success + failed);
        report.setSuccess(success);
        report.setFailed(failed);
        report.setUploadedAt(LocalDateTime.now().toString());
        return report;
    }
}

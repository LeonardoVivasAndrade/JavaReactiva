package co.com.bancolombia.api;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.usecase.closebox.CloseboxUseCase;
import co.com.bancolombia.usecase.createbox.CreateboxUseCase;
import co.com.bancolombia.usecase.deletebox.DeleteBoxUseCase;
import co.com.bancolombia.usecase.getboxbyid.GetBoxByIdUseCase;
import co.com.bancolombia.usecase.listallboxes.ListAllBoxesUseCase;
import co.com.bancolombia.usecase.openbox.OpenboxUseCase;
import co.com.bancolombia.usecase.reopenbox.ReopenBoxUseCase;
import co.com.bancolombia.usecase.updateboxname.UpdateBoxNameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Handler {
    private final CreateboxUseCase createboxUseCase;
    private final OpenboxUseCase openboxUseCase;
    private final CloseboxUseCase closeboxUseCase;
    private final GetBoxByIdUseCase getBoxByIdUseCase;
    private final ListAllBoxesUseCase listAllBoxesUseCase;
    private final UpdateBoxNameUseCase updateBoxNameUseCase;
    private final DeleteBoxUseCase deleteBoxUseCase;
    private final ReopenBoxUseCase reopenBoxUseCase;


    public Mono<ServerResponse> createBox(ServerRequest serverRequest) {
        String createdBy = serverRequest.headers().firstHeader("created-by");
        return serverRequest
                .bodyToMono(Box.class)
                .flatMap(box -> createboxUseCase.create(box.getId(), box.getName(), createdBy.toLowerCase()))
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)));
    }

    public Mono<ServerResponse> openBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return openboxUseCase.openBox(id, BigDecimal.ZERO)
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> closeBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return closeboxUseCase.closeBox(id, BigDecimal.ZERO)
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findBoxById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return getBoxByIdUseCase.getBoxById(id)
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listAllBoxes(ServerRequest request) {
        String statusParam = request.queryParam("status").orElse(null);
        String openedAfterParam = request.queryParam("openedAfter").orElse(null);
        String createdBy = request.queryParam("createdBy").orElse(null);

        BoxStatus status = null;
        LocalDateTime openedAfter = null;

        try {
            if (statusParam != null) {
                status = BoxStatus.valueOf(statusParam.toUpperCase());
            }
            if (openedAfterParam != null) {
                openedAfter = LocalDateTime.parse(openedAfterParam);
            }
        } catch (Exception e) {
            return ServerResponse.badRequest().bodyValue("Parámetros inválidos: " + e.getMessage());
        }

        return listAllBoxesUseCase.listAllBoxes(status, openedAfter, createdBy)
                .collectList()
                .flatMap(boxes -> ServerResponse.ok().bodyValue(boxes));
    }

    public Mono<ServerResponse> updateBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return serverRequest
                .bodyToMono(Box.class)
                .flatMap(box -> updateBoxNameUseCase.update(id, box.getName()))
                .flatMap(boxUpdated -> ServerResponse.ok().body(BodyInserters.fromValue(boxUpdated)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        String responsible = serverRequest.headers().firstHeader("responsible");

        return deleteBoxUseCase.delete(id, responsible)
                .flatMap(boxDeleted -> ServerResponse.ok().body(BodyInserters.fromValue(boxDeleted)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> reopenBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        String responsible = serverRequest.headers().firstHeader("responsible");

        return reopenBoxUseCase.reopen(id, responsible)
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}

package co.com.bancolombia.api;

import co.com.bancolombia.api.exception.InvalidFileException;
import co.com.bancolombia.model.movement.MovementUploadReport;
import co.com.bancolombia.usecase.uploadmovements.UploadMovementsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.List;

import static co.com.bancolombia.api.helpers.Hash.calculatefromFlux;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovementHandler {
    private final UploadMovementsUseCase uploadMovementsUseCase;

    public Mono<ServerResponse> uploadCSV(ServerRequest serverRequest) {
        String boxId = serverRequest.pathVariable("boxId");
        String uploadedBy = serverRequest.headers().firstHeader("user");

        return serverRequest.multipartData()
                .flatMap(parts -> {
                    Part filePart = parts.toSingleValueMap().get("file");

                    if (filePart == null) return Mono.error(new InvalidFileException("El archivo es requerido"));
                    if (!MediaType.TEXT_PLAIN.equals(filePart.headers().getContentType()) &&
                            !MediaType.valueOf("text/csv").equals(filePart.headers().getContentType())) {
                        return Mono.error(new InvalidFileException("El archivo debe ser de tipo text/csv"));
                    }

                    FilePart file = (FilePart) filePart;
                    String originalFileName = file.filename();

                    return file.content()
                            .collectList()
                            .flatMap(dataBuffers -> {
                                // Calcular tamaño archivo
                                int totalSize = dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum();
                                if (totalSize > 5 * 1024 * 1024) {
                                    return Mono.error(new InvalidFileException("El archivo no debe superar los 5MB"));
                                }
                                String sizeFile = totalSize + " bytes";

                                // Clonar buffers para hash y upload
                                List<ByteBuffer> forHash = dataBuffers.stream().map(dataBuffer -> {
                                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(bytes);
                                    return ByteBuffer.wrap(bytes);
                                }).toList();

                                List<ByteBuffer> forUpload = forHash.stream()
                                        .map(buffer -> {
                                            ByteBuffer duplicate = ByteBuffer.allocate(buffer.remaining());
                                            duplicate.put(buffer.duplicate());
                                            duplicate.flip();
                                            return duplicate;
                                        }).toList();

                                // Calcular hash y subir archivo
                                return calculatefromFlux(Flux.fromIterable(forHash))
                                        .flatMap(hashFile -> uploadMovementsUseCase.uploadCSV(Flux.fromIterable(forUpload),
                                                        MovementUploadReport.builder()
                                                                .boxId(boxId)
                                                                .uploadedBy(uploadedBy)
                                                                .originalFileName(originalFileName)
                                                                .sizeFile(sizeFile)
                                                                .hashFile(hashFile)
                                                                .build()
                                                )
                                                .flatMap(uploadReport -> ServerResponse.ok()
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .bodyValue(uploadReport)));
                            });
                });
    }
}

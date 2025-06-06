package com.example.webfluxintro.controller;

import com.example.webfluxintro.dto.TransferenciaDTO;
import com.example.webfluxintro.rest.TransferenciaRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class TransferController {
    List<String> permitidos = List.of("JUAN", "LAURA", "CARLOS");

    @PostMapping("/procesar-transferencias")
    public Flux<String> procesarTransferencias(@RequestBody Mono<TransferenciaRequest> requestMono) {

        return requestMono
                .flatMapMany(transferenciaRequest ->
                        Flux.fromIterable(transferenciaRequest.getTransferencias())
                )
                .filter(transferenciaDTO ->
                        transferenciaDTO.getDestinatario() != null && !transferenciaDTO.getDestinatario().isBlank()
                )
                .filter(transferenciaDTO ->
                        transferenciaDTO.getMonto() > 0
                )
                .delayElements(Duration.ofMillis(500))
                //.map el map se aplica cuando la validacion no tiene nada que ver con otro flujo
                .flatMap(this::validarTransferencia)
                .log();
    }

    private Mono<String> validarTransferencia(TransferenciaDTO t){
        return Mono.delay(Duration.ofMillis(300))
                .map( d -> {
                    if(!permitidos.contains(t.getDestinatario().toUpperCase())) {
                        return "❌ Transferencia rechazada: " + t.getDestinatario() + " no es un destinatario autorizado.\n";
                    }
                    if(t.getMonto() > 5000.0){
                        return "❌ Transferencia rechazada: monto excede el límite permitido ($5000) para " + t.getDestinatario()+"\n";
                    }
                    return "✅ Transferencia aprobada a "+ t.getDestinatario() + " por $"+t.getMonto()+"\n";
                });
    }

}

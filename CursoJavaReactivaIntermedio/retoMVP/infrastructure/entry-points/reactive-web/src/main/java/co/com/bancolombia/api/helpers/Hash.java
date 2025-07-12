package co.com.bancolombia.api.helpers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Hash {

    public static Mono<String> calculatefromFlux(Flux<ByteBuffer> byteBufferFlux) {
        return byteBufferFlux
                .collectList()
                .flatMap(buffers -> Mono.fromCallable(() -> {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    for (ByteBuffer buffer : buffers) {
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        baos.write(bytes);
                    }

                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(baos.toByteArray());

                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }

                    return hexString.toString();
                }).subscribeOn(Schedulers.boundedElastic()));
    }
}

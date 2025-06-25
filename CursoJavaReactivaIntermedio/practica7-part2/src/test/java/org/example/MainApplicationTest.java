package org.example;

import org.example.dto.CashRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MainApplicationTest {

    @Autowired
    WebTestClient client;

    @Test
    void cashInTest(){
        CashRequestDto req = new CashRequestDto(
                BigDecimal.valueOf(100),
                "USD",
                "ext-123"
        );

        client.post().uri("/cash-in")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("POSTED");
    }

    @Test
    void cashOutTest(){
        Map<String, Object> req = Map.of("amount", 100, "currency", "USD");

        client.post().uri("/cash-out")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("POSTED");
    }

    @Test
    void findByTest() {
        Map<String, Object> req = Map.of("amount", 100, "currency", "USD");

        client.post().uri("/cash-out")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").value(id -> {
                    client.get().uri("/transactions/{id}", id)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .jsonPath("$.id").isEqualTo(id)
                            .jsonPath("$.status").isEqualTo("POSTED"); // o el estado esperado
                });
    }
}
package co.com.bancolombia.mongo.model;

import co.com.bancolombia.model.box.BoxStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document
@Builder(toBuilder = true)
public class MovementDocument {
    @Id
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String currency;
    private String description;
}

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
public class BoxDocument {
    @Id
    private String id;
    private String name;
    private BoxStatus status;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private String createdAt;
    private String createdBy;
    private BigDecimal currentBalance;
}

package co.com.bancolombia.model.movement;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Movement {
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String currency;
    private String description;

    private static final Set<String> CURRENCIES = Set.of("COP", "USD");
    private static final Set<String> MOVEMENTS_TYPE = Set.of("INCOME", "EXPENSE");

    public static class MovementBuilder {
        public MovementBuilder movementId(String movementId) {
            if (isNullOrEmpty(movementId)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'movementId' no puede ser nulo ni estar vacío.");
            this.movementId = movementId;
            return this;
        }

        public MovementBuilder boxIdSrt(String boxIdRecord, String boxId) {
            if (isNullOrEmpty(boxIdRecord)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'boxId' no puede ser nulo ni estar vacío.");
            if (!boxIdRecord.equals(boxId)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'boxId': "+boxIdRecord+" no es igual al del request: "+boxId);
            this.boxId = boxIdRecord;
            return this;
        }

        public MovementBuilder dateIso(String date) {
            if (isNullOrEmpty(date)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'date' no puede ser nulo ni estar vacío.");
            try {
                this.date = LocalDateTime.parse(date);
            } catch (Exception e) {
                throw new IllegalArgumentException("Movimiento[" + movementId + "] : " +
                        "Fecha inválida, debe estar en formato ISO 8601: " + date);
            }
            return this;
        }

        public MovementBuilder type(String type) {
            if (isNullOrEmpty(type)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'type' no puede ser nulo ni estar vacío.");
            if (!MOVEMENTS_TYPE.contains(type.toUpperCase())) {
                String alloweds = "[%s]".formatted(String.join(" | ", MOVEMENTS_TYPE));
                throw new IllegalArgumentException("Movimiento[" + movementId + "] : " +
                        "Tipo de movimiento no válido: " + type + " - Permitidos: " + alloweds);
            }
            this.type = type;
            return this;
        }

        public MovementBuilder amountStr(String amount) {
            if (isNullOrEmpty(amount)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'amount' no puede ser nulo ni estar vacío.");
            if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El monto debe ser positivo: " + amount);
            this.amount =  new BigDecimal(amount);
            return this;
        }

        public MovementBuilder currency(String currency) {
            if (isNullOrEmpty(currency)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'currency' no puede ser nulo ni estar vacío.");
            if (!CURRENCIES.contains(currency.toUpperCase())) {
                String alloweds = "[%s]".formatted(String.join(" | ", CURRENCIES));
                throw new IllegalArgumentException("Movimiento[" + movementId + "] : " +
                        "Tipo de moneda no es válido: " + currency + " - Permitidos: " + alloweds);
            }
            this.currency = currency;
            return this;
        }

        public MovementBuilder description(String description) {
            if (isNullOrEmpty(description)) throw new IllegalArgumentException("Movimiento[" + movementId + "] : El campo 'description' no puede ser nulo ni estar vacío.");
            this.description = description;
            return this;
        }

        private boolean isNullOrEmpty(String value) {
            return value == null || value.trim().isEmpty();
        }
    }
}

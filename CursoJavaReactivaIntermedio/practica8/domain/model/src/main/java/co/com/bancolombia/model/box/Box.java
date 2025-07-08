package co.com.bancolombia.model.box;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Box {
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

    //METODOS DE NEFOCIO (SIN CAMBIOS)
    public void open(BigDecimal amount) {
        if (status == BoxStatus.OPENED)
            throw new IllegalStateException("La caja ya está abierta");

        this.openingAmount = amount;
        this.currentBalance = amount;
        this.status = BoxStatus.OPENED;
        this.openedAt = LocalDateTime.now();
    }

    public void close(BigDecimal closingAmount) {
        if (status != BoxStatus.OPENED)
            throw new IllegalStateException("La caja no está abierta");

        this.closingAmount = closingAmount;
        this.status = BoxStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    public void addAmount(BigDecimal amount) {
        if (status != BoxStatus.OPENED)
            throw new IllegalStateException("La caja debe estar abierta oara regustrar movimientos");

        this.currentBalance = this.currentBalance.add(amount);
    }

    public void subtractAmount(BigDecimal amount) {
        if (status != BoxStatus.OPENED)
            throw new IllegalStateException("La caja debe estar abierta oara regustrar movimientos");
        if (this.currentBalance.compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente");

        this.currentBalance = this.currentBalance.subtract(amount);
    }

    public void reopen(BigDecimal amount) {
        if (status == BoxStatus.OPENED)
            throw new IllegalStateException("Solo se puede reabrir una caja cerrada");

        if (status == BoxStatus.DELETED)
            throw new IllegalStateException("No se puede abrir una caja eliminada");

        this.status = BoxStatus.OPENED;
        this.openedAt = LocalDateTime.now();
    }
}

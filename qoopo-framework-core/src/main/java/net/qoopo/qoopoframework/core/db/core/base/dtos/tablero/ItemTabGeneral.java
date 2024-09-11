package net.qoopo.qoopoframework.core.db.core.base.dtos.tablero;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO generico usado para cargar la informacion en los tableros
 *
 * @author alberto
 */
@Getter
@Setter
public class ItemTabGeneral implements Serializable {

    private String cadena;
    private int tipo;
    private int estado;
    private LocalDateTime fecha;
    private LocalDateTime fecha2;
    private BigDecimal monto;
    private BigDecimal monto2;
    private boolean valorBoleano;

    public ItemTabGeneral(String cadena, BigDecimal monto) {
        this.cadena = cadena;
        this.monto = monto;
    }

    public ItemTabGeneral(String cadena, BigDecimal monto, BigDecimal monto2) {
        this.cadena = cadena;
        this.monto = monto;
        this.monto2 = monto2;
    }

    public ItemTabGeneral(String cadena, int tipo, int estado, LocalDateTime fecha, BigDecimal monto) {
        this.cadena = cadena;
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.monto = monto;
    }

    public ItemTabGeneral(int tipo, int estado, LocalDateTime fecha, BigDecimal monto) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.monto = monto;
    }

    public ItemTabGeneral(int tipo, int estado, LocalDateTime fecha, BigDecimal monto, boolean conciliado) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.monto = monto;
        this.valorBoleano = conciliado;
    }

    public ItemTabGeneral(int tipo, int estado, LocalDateTime fecha, LocalDateTime fecha2, BigDecimal monto,
            boolean valorBoleano) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.fecha2 = fecha2;
        this.monto = monto;
        this.valorBoleano = valorBoleano;
    }

    public ItemTabGeneral(int tipo, int estado, LocalDateTime fecha, LocalDateTime fecha2, BigDecimal monto) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.fecha2 = fecha2;
        this.monto = monto;
    }

    public ItemTabGeneral(int tipo, int estado, LocalDateTime fecha, LocalDateTime fecha2) {
        this.tipo = tipo;
        this.estado = estado;
        this.fecha = fecha;
        this.fecha2 = fecha2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.tipo;
        hash = 47 * hash + this.estado;
        hash = 47 * hash + Objects.hashCode(this.fecha);
        hash = 47 * hash + Objects.hashCode(this.monto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemTabGeneral other = (ItemTabGeneral) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.estado != other.estado) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        return Objects.equals(this.monto, other.monto);
    }

}

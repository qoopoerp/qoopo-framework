package net.qoopo.qoopoframework.core.db.filtro.condicion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.core.db.core.base.dtos.Estado;
import net.qoopo.util.date.FechasUtil;

@Getter
@Setter
public class Valor implements Serializable {

    // private static final Random random = new Random();
    private static final SecureRandom random = new SecureRandom();

    public static final Valor ZERO = new Valor(BigDecimal.ZERO);

    // 1 string, 2 boolean, 3 bigdecimal, 4 long, 5 entero, 6 fecha, para uso
    // interno
    private final int tipo;
    private String nombre;// el nombe con el que irá al la consulta (ejemplo :nombre o :fecha)
    private String valorString;
    private Boolean valorBoolean;
    private BigDecimal valorNumerico;
    private Long valorLong;
    private Integer valorInteger;
    private LocalDateTime valorFecha;

    public Valor(String valorString) {
        this.nombre = "value" + getRandomId();
        this.valorString = valorString;
        tipo = Campo.STRING;
    }

    public Valor(String nombre, String valorString) {
        this.nombre = nombre + getRandomId();
        this.valorString = valorString;
        tipo = Campo.STRING;
    }

    public Valor(Boolean valorBoolean) {
        this.nombre = "value" + getRandomId();
        this.valorBoolean = valorBoolean;
        tipo = Campo.BOLEANO;
    }

    public Valor(String nombre, Boolean valorBoolean) {
        this.nombre = nombre + getRandomId();
        this.valorBoolean = valorBoolean;
        tipo = Campo.BOLEANO;
    }

    public Valor(BigDecimal valorNumerico) {
        this.nombre = "value" + getRandomId();
        this.valorNumerico = valorNumerico;
        tipo = Campo.NUMERICO;
    }

    public Valor(String nombre, BigDecimal valorNumerico) {
        this.nombre = nombre + getRandomId();
        this.valorNumerico = valorNumerico;
        tipo = Campo.NUMERICO;
    }

    public Valor(Date valorFecha) {
        this.nombre = "value" + getRandomId();
        this.valorFecha = FechasUtil.convertToLocalDateTimeViaInstant(valorFecha);
        tipo = Campo.FECHA;
    }

    public Valor(String nombre, Date valorFecha) {
        this.nombre = nombre + getRandomId();
        this.valorFecha = FechasUtil.convertToLocalDateTimeViaInstant(valorFecha);
        tipo = Campo.FECHA;
    }

    public Valor(LocalDateTime valorFecha) {
        this.nombre = "value" + getRandomId();
        this.valorFecha = valorFecha;
        tipo = Campo.FECHA;
    }

    public Valor(String nombre, LocalDateTime valorFecha) {
        this.nombre = nombre + getRandomId();
        this.valorFecha = valorFecha;
        tipo = Campo.FECHA;
    }

    public Valor(Long valorLong) {
        this.nombre = "value" + getRandomId();
        this.valorLong = valorLong;
        tipo = Campo.LONG;
    }

    public Valor(String nombre, Long valorLong) {
        this.nombre = nombre + getRandomId();
        this.valorLong = valorLong;
        tipo = Campo.LONG;
    }

    public Valor(String nombre, Integer valorInteger) {
        this.nombre = nombre + getRandomId();
        this.valorInteger = valorInteger;
        tipo = Campo.INTEGER;
    }

    public Valor(Integer valorInteger) {
        this.nombre = "value" + getRandomId();
        this.valorInteger = valorInteger;
        tipo = Campo.INTEGER;
    }

    public Valor(String nombre, Estado valorEstado) {
        this.nombre = nombre + getRandomId();
        this.valorInteger = valorEstado.getValor();
        tipo = Campo.INTEGER;
    }

    public Valor(Estado valorEstado) {
        this.nombre = "value" + getRandomId();
        this.valorInteger = valorEstado.getValor();
        tipo = Campo.INTEGER;
    }

    public void setValor(Object valor) {
        switch (tipo) {
            case Campo.STRING:
                setValorString((String) valor);
                break;
            case Campo.BOLEANO:
                setValorBoolean((Boolean) valor);
                break;
            case Campo.NUMERICO:
                setValorNumerico((BigDecimal) valor);
                break;
            case Campo.LONG:
                setValorLong((Long) valor);
                break;
            case Campo.INTEGER:
                setValorInteger((Integer) valor);
                break;
            case Campo.FECHA:
                setValorFecha((LocalDateTime) valor);
                break;
            default:
                break;
        }
    }

    public Object getValor() {
        switch (tipo) {
            case Campo.STRING:
                return getValorString();
            case Campo.BOLEANO:
                return getValorBoolean();
            case Campo.NUMERICO:
                return getValorNumerico();
            case Campo.LONG:
                return getValorLong();
            case Campo.INTEGER:
                return getValorInteger();
            case Campo.FECHA:
                return getValorFecha();
            default:
                return null;
        }
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public Boolean getValorBoolean() {
        return valorBoolean;
    }

    public void setValorBoolean(Boolean valorBoolean) {
        this.valorBoolean = valorBoolean;
    }

    public BigDecimal getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(BigDecimal valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public LocalDateTime getValorFecha() {
        return valorFecha;
    }

    public void setValorFecha(LocalDateTime valorFecha) {
        this.valorFecha = valorFecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre + getRandomId();
    }

    public Long getValorLong() {
        return valorLong;
    }

    public void setValorLong(Long valorLong) {
        this.valorLong = valorLong;
    }

    public Integer getValorInteger() {
        return valorInteger;
    }

    public void setValorInteger(Integer valorInteger) {
        this.valorInteger = valorInteger;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.tipo;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.valorString);
        hash = 67 * hash + Objects.hashCode(this.valorBoolean);
        hash = 67 * hash + Objects.hashCode(this.valorNumerico);
        hash = 67 * hash + Objects.hashCode(this.valorLong);
        hash = 67 * hash + Objects.hashCode(this.valorInteger);
        hash = 67 * hash + Objects.hashCode(this.valorFecha);
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
        final Valor other = (Valor) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.valorString, other.valorString)) {
            return false;
        }
        if (!Objects.equals(this.valorBoolean, other.valorBoolean)) {
            return false;
        }
        if (!Objects.equals(this.valorNumerico, other.valorNumerico)) {
            return false;
        }
        if (!Objects.equals(this.valorLong, other.valorLong)) {
            return false;
        }
        if (!Objects.equals(this.valorInteger, other.valorInteger)) {
            return false;
        }
        if (!Objects.equals(this.valorFecha, other.valorFecha)) {
            return false;
        }
        return true;
    }

    private int getRandomId() {
        return random.nextInt(100);
    }

    @Override
    public String toString() {
        return "Valor{" + "tipo=" + tipo + ", nombre=" + nombre + ", valorString=" + valorString + ", valorBoolean="
                + valorBoolean + ", valorNumerico=" + valorNumerico + ", valorLong=" + valorLong + ", valorInteger="
                + valorInteger + ", valorFecha=" + valorFecha + '}';
    }

}

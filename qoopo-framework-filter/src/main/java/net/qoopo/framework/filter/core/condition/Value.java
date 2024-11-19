package net.qoopo.framework.filter.core.condition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.date.FechasUtil;

@Getter
@Setter
public class Value implements Serializable {

    private static final SecureRandom random = new SecureRandom();

    public static final Value ZERO = new Value(BigDecimal.ZERO);

    // 1 string, 2 boolean, 3 bigdecimal, 4 long, 5 entero, 6 fecha, para uso
    // interno
    private final int tipo;
    private String name;// el nombe con el que irá al la consulta (ejemplo :nombre o :fecha)
    private String valorString;
    private Boolean valorBoolean;
    private BigDecimal valorNumerico;
    private Long valorLong;
    private Integer valorInteger;
    private LocalDateTime valorFecha;

    public Value(String valorString) {
        this.name = "value" + getRandomId();
        this.valorString = valorString;
        tipo = Field.STRING;
    }

    public Value(String nombre, String valorString) {
        this.name = nombre + getRandomId();
        this.valorString = valorString;
        tipo = Field.STRING;
    }

    public Value(Boolean valorBoolean) {
        this.name = "value" + getRandomId();
        this.valorBoolean = valorBoolean;
        tipo = Field.BOLEANO;
    }

    public Value(String nombre, Boolean valorBoolean) {
        this.name = nombre + getRandomId();
        this.valorBoolean = valorBoolean;
        tipo = Field.BOLEANO;
    }

    public Value(BigDecimal valorNumerico) {
        this.name = "value" + getRandomId();
        this.valorNumerico = valorNumerico;
        tipo = Field.NUMERICO;
    }

    public Value(String nombre, BigDecimal valorNumerico) {
        this.name = nombre + getRandomId();
        this.valorNumerico = valorNumerico;
        tipo = Field.NUMERICO;
    }

    public Value(Date valorFecha) {
        this.name = "value" + getRandomId();
        this.valorFecha = FechasUtil.convertToLocalDateTimeViaInstant(valorFecha);
        tipo = Field.FECHA;
    }

    public Value(String nombre, Date valorFecha) {
        this.name = nombre + getRandomId();
        this.valorFecha = FechasUtil.convertToLocalDateTimeViaInstant(valorFecha);
        tipo = Field.FECHA;
    }

    public Value(LocalDateTime valorFecha) {
        this.name = "value" + getRandomId();
        this.valorFecha = valorFecha;
        tipo = Field.FECHA;
    }

    public Value(String nombre, LocalDateTime valorFecha) {
        this.name = nombre + getRandomId();
        this.valorFecha = valorFecha;
        tipo = Field.FECHA;
    }

    public Value(Long valorLong) {
        this.name = "value" + getRandomId();
        this.valorLong = valorLong;
        tipo = Field.LONG;
    }

    public Value(String nombre, Long valorLong) {
        this.name = nombre + getRandomId();
        this.valorLong = valorLong;
        tipo = Field.LONG;
    }

    public Value(String nombre, Integer valorInteger) {
        this.name = nombre + getRandomId();
        this.valorInteger = valorInteger;
        tipo = Field.INTEGER;
    }

    public Value(Integer valorInteger) {
        this.name = "value" + getRandomId();
        this.valorInteger = valorInteger;
        tipo = Field.INTEGER;
    }

    // public Value(String nombre, Estado valorEstado) {
    // this.nombre = nombre + getRandomId();
    // this.valorInteger = valorEstado.getValor();
    // tipo = Field.INTEGER;
    // }

    // public Value(Estado valorEstado) {
    // this.nombre = "value" + getRandomId();
    // this.valorInteger = valorEstado.getValor();
    // tipo = Field.INTEGER;
    // }

    public void setValor(Object valor) {
        switch (tipo) {
            case Field.STRING:
                setValorString((String) valor);
                break;
            case Field.BOLEANO:
                setValorBoolean((Boolean) valor);
                break;
            case Field.NUMERICO:
                setValorNumerico((BigDecimal) valor);
                break;
            case Field.LONG:
                setValorLong((Long) valor);
                break;
            case Field.INTEGER:
                setValorInteger((Integer) valor);
                break;
            case Field.FECHA:
                setValorFecha((LocalDateTime) valor);
                break;
            default:
                break;
        }
    }

    public Object get() {
        switch (tipo) {
            case Field.STRING:
                return getValorString();
            case Field.BOLEANO:
                return getValorBoolean();
            case Field.NUMERICO:
                return getValorNumerico();
            case Field.LONG:
                return getValorLong();
            case Field.INTEGER:
                return getValorInteger();
            case Field.FECHA:
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

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre + getRandomId();
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
        hash = 67 * hash + Objects.hashCode(this.name);
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
        final Value other = (Value) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
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
        return "Valor{" + "tipo=" + tipo + ", nombre=" + name + ", valorString=" + valorString + ", valorBoolean="
                + valorBoolean + ", valorNumerico=" + valorNumerico + ", valorLong=" + valorLong + ", valorInteger="
                + valorInteger + ", valorFecha=" + valorFecha + '}';
    }

    public static Value of(String value) {
        return new Value(value);
    }

    public static Value of(String name, String value) {
        return new Value(name, value);
    }

    public static Value of(Boolean value) {
        return new Value(value);
    }

    public static Value of(String name, Boolean value) {
        return new Value(name, value);
    }

    public static Value of(BigDecimal value) {
        return new Value(value);
    }

    public static Value of(String name, BigDecimal value) {
        return new Value(name, value);
    }

    public static Value of(Date value) {
        return new Value(value);
    }

    public static Value of(String name, Date value) {
        return new Value(name, value);
    }

    public static Value of(LocalDateTime value) {
        return new Value(value);
    }

    public static Value of(String name, LocalDateTime value) {
        return new Value(name, value);
    }


    public static Value of(Long value) {
        return new Value(value);
    }

    public static Value of(String name,Long value) {
        return new Value(name, value);
    }

    public static Value of(Integer value) {
        return new Value(value);
    }

    public static Value of(String name,Integer value) {
        return new Value(name, value);
    }
}

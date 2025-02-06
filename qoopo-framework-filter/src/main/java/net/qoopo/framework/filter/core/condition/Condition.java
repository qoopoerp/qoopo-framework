package net.qoopo.framework.filter.core.condition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Condition implements Serializable {
    public static final Logger log = Logger.getLogger("Qoopo");

    public static final int RELACION_AND = 1;

    public static final int RELACION_OR = 2;

    public static final int RELACION_EMPTY = 0;

    public static final List<Condition> CONDICIONES = new ArrayList<>();

    public static Condition EMPTY = new Condition();

    private Long id;

    private Field field;

    private Function function;

    public static Condition of(Field field, Function function) {
        return Condition.builder().field(field).function(function).build();
    }

    public static Condition of(String name, Field field, Function function) {
        return Condition.builder().name(name).field(field).function(function).build();
    }

    public static Condition of(Field field, Function function, Value value) {
        return Condition.builder().field(field).function(function).value(value).build();
    }

    public static Condition of(String name, Field field, Function function, Value value) {
        return Condition.builder().name(name).field(field).function(function).value(value).build();
    }

    public static Condition of(Field field, Function function, Value value, Value value2) {
        return Condition.builder().field(field).function(function).value(value).value2(value2).build();
    }

    public static Condition of(String name, Field field, Function function, Value value, Value value2) {
        return Condition.builder().name(name).field(field).function(function).value(value).value2(value2).build();
    }

    public static Condition getOpen() {
        Condition condicion = new Condition();
        condicion.setOpen(true);
        return condicion;
    }

    public static Condition getClose() {
        Condition condicion = new Condition();
        condicion.setClose(true);
        return condicion;
    }

    public Condition open() {
        Condition condicion = new Condition();
        condicion.setOpen(true);
        return concat(condicion);
    }

    public Condition close() {
        Condition condicion = new Condition();
        condicion.setClose(true);
        return concat(condicion);
    }

    private Value value;

    private Value value2;

    private Condition next;

    private boolean open;

    private boolean close;

    private boolean token_and;

    private boolean token_or;
    @Builder.Default
    protected String name = "";

    @Builder.Default
    protected boolean exclusivo = false;

    public Condition() {

    }

    public Condition setName(String nombre) {
        this.name = nombre;
        return this;
    }

    public Condition getNext() {
        return this.next;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean abrir) {
        this.open = abrir;
    }

    public boolean isClose() {
        return this.close;
    }

    public void setClose(boolean cerrar) {
        this.close = cerrar;
    }

    public boolean isToken_and() {
        return this.token_and;
    }

    public void setToken_and(boolean token_and) {
        this.token_and = token_and;
    }

    public boolean isToken_or() {
        return this.token_or;
    }

    public void setToken_or(boolean token_or) {
        this.token_or = token_or;
    }

    public List<Condition> getListaCondiciones() {
        List<Condition> tmp = new ArrayList<>();
        tmp.add(this);
        if (this.next != null)
            tmp.addAll(this.next.getListaCondiciones());
        return tmp;
    }

    public Condition and() {
        Condition condicion = new Condition();
        condicion.setName("and");
        condicion.setToken_and(true);
        return concat(condicion);
    }

    public Condition or() {
        Condition condicion = new Condition();
        condicion.setName("or");
        condicion.setToken_or(true);
        return concat(condicion);
    }

    public Condition concat(Condition condicion) {
        if (this.next == null) {
            setNext(condicion);
        } else {
            getNext().concat(condicion);
        }
        return this;
    }

    public Condition and(Condition condicion) {
        return and().concat(condicion);
    }

    public Condition or(Condition condicion) {
        return or().concat(condicion);
    }

    public boolean containsCondition(Condition condicion) {
        if (condicion != null && condicion.equals(this))
            return true;
        if (this.next == null)
            return false;
        return getNext().containsCondition(condicion);
    }

    public void setNext(Condition siguiente) {
        this.next = siguiente;
    }

    public String buildQuery() {
        try {
            StringBuilder salida = new StringBuilder("");
            if (this.open) {
                salida.append("(");
            } else if (this.close) {
                salida.append(")");
            } else if (this.token_and) {
                salida.append(" and ");
            } else if (this.token_or) {
                salida.append(" or ");
            } else {
                int nElements = 1;
                salida.append(" (");
                if (this.field != null)
                    nElements = (this.field.getNombreJpa().split(",")).length;
                for (int i = 0; i < nElements; i++) {
                    if (this.function != null)
                        salida.append(this.function.getAntesCampo());
                    if (this.field != null)
                        salida.append(this.field.getNombreJpa().split(",")[i].trim()).append(" ");
                    if (this.function != null) {
                        salida.append(this.function.getDespuesCampo());
                        salida.append(this.function.getNombreJpa()).append(" ");
                    }
                    if (this.value != null)
                        salida.append(":").append(this.value.getName());
                    if (this.value2 != null) {
                        salida.append(" and ");
                        salida.append(":").append(this.value2.getName());
                    }
                    if (i < nElements - 1)
                        salida.append(" or ");
                }
                salida.append(") ");
            }
            if (this.next != null)
                salida.append(this.next.buildQuery());
            return salida.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public String getName() {
        if (this.name == null || this.name.isEmpty()) {
            if (this.open)
                return "abrir";
            if (this.close)
                return "cerrar";
            if (this.token_and)
                return "and";
            if (this.token_or)
                return "or";
            return this.field.getNombre().concat(" ").concat(this.function.getNombre()).concat(" ")
                    .concat((this.value != null && this.value.get() != null) ? this.value.get().toString()
                            : "")
                    .concat((this.value2 != null && this.value2.get() != null) ? this.value2.get().toString()
                            : "");
        }
        return this.name;
    }

    public String toString() {
        return "Condition {campo=" + this.field + ", funcion=" + this.function + ", valor1=" + this.value + ", valor2="
                + this.value2 + ", siguiente=" + this.next + ", abrir=" + this.open + ", cerrar=" + this.close
                + ", token_and=" + this.token_and + ", token_or=" + this.token_or + ", nombre=" + this.name
                + ", exclusivo=" + this.exclusivo + "}";
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.field);
        hash = 41 * hash + Objects.hashCode(this.function);
        hash = 41 * hash + Objects.hashCode(this.value);
        hash = 41 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Condition other = (Condition) obj;
        if (!Objects.equals(this.name, other.name))
            return false;
        if (!Objects.equals(this.field, other.field))
            return false;
        if (!Objects.equals(this.function, other.function))
            return false;
        return Objects.equals(this.value, other.value);
    }

    public Condition clonar() {
        Condition nuevo = new Condition();
        nuevo.setOpen(this.open);
        nuevo.setClose(this.close);
        nuevo.setName(this.name);
        nuevo.setField(this.field);
        nuevo.setFunction(this.function);
        nuevo.setToken_and(this.token_and);
        nuevo.setToken_or(this.token_or);
        nuevo.setValue(this.value);
        nuevo.setValue2(this.value2);
        nuevo.setExclusivo(this.exclusivo);
        if (this.next != null)
            nuevo.setNext(this.next.clonar());
        return nuevo;
    }

    public Condition exclusivo() {
        this.exclusivo = true;
        return this;
    }

}

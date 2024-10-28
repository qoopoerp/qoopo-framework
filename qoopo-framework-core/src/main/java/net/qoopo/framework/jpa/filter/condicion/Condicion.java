package net.qoopo.framework.jpa.filter.condicion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.framework.db.jpa.Parametro;
import net.qoopo.framework.jpa.core.interfaces.Duplicable;
import net.qoopo.framework.util.QoopoBuilder;

public class Condicion implements Duplicable, Serializable {
    public static final Logger log = Logger.getLogger("Qoopo");

    public static final int RELACION_AND = 1;

    public static final int RELACION_OR = 2;

    public static final int RELACION_EMPTY = 0;

    public static final List<Condicion> CONDICIONES = new ArrayList<>();

    public static Builder getBuilder() {
        return new Builder();
    }

    public static Condicion EMPTY = new Condicion();

    private Campo campo;

    private Funcion funcion;

    public static Condicion open() {
        Condicion condicion = new Condicion();
        condicion.setAbrir(true);
        return condicion;
    }

    public static Condicion close() {
        Condicion condicion = new Condicion();
        condicion.setCerrar(true);
        return condicion;
    }

    public static Condicion build(Campo campo, Funcion funcion) {
        return getBuilder().campo(campo).funcion(funcion).build();
    }

    public static Condicion build(String nombre, Campo campo, Funcion funcion) {
        return getBuilder().nombre(nombre).campo(campo).funcion(funcion).build();
    }

    public static Condicion build(Campo campo, Funcion funcion, Valor valor) {
        return getBuilder().campo(campo).funcion(funcion).valor(valor).build();
    }

    public static Condicion build(Campo campo, Funcion funcion, Valor valor, Valor valor2) {
        return getBuilder().campo(campo).funcion(funcion).valor(valor).valor2(valor2).build();
    }

    public static Condicion build(String nombre, Campo campo, Funcion funcion, Valor valor) {
        return getBuilder().nombre(nombre).campo(campo).funcion(funcion).valor(valor).build();
    }

    public static Condicion build(String nombre, Campo campo, Funcion funcion, Valor valor, Valor valor2) {
        return getBuilder().nombre(nombre).campo(campo).funcion(funcion).valor(valor).valor2(valor2).build();
    }

    private Valor valor1 = null;

    private Valor valor2 = null;

    private Condicion siguiente;

    private boolean abrir;

    private boolean cerrar;

    private boolean token_and;

    private boolean token_or;

    protected String nombre = "";

    protected String pruebas;

    protected boolean exclusivo = false;

    public Condicion() {
        this.pruebas = "miPrueba";
    }

    private Condicion(Campo campo, Funcion funcion, Valor valor1, Valor valor2, Condicion siguiente) {
        this.campo = campo;
        this.funcion = funcion;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.siguiente = siguiente;
    }

    public boolean isExclusivo() {
        return this.exclusivo;
    }

    public void setExclusivo(boolean exclusivo) {
        this.exclusivo = exclusivo;
    }

    public Condicion setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Campo getCampo() {
        return this.campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public Funcion getFuncion() {
        return this.funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Valor getValor1() {
        return this.valor1;
    }

    public void setValor1(Valor valor1) {
        this.valor1 = valor1;
    }

    public Valor getValor2() {
        return this.valor2;
    }

    public void setValor2(Valor valor2) {
        this.valor2 = valor2;
    }

    public Condicion getSiguiente() {
        return this.siguiente;
    }

    public boolean isAbrir() {
        return this.abrir;
    }

    public void setAbrir(boolean abrir) {
        this.abrir = abrir;
    }

    public boolean isCerrar() {
        return this.cerrar;
    }

    public void setCerrar(boolean cerrar) {
        this.cerrar = cerrar;
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

    public List<Condicion> getListaCondiciones() {
        List<Condicion> tmp = new ArrayList<>();
        tmp.add(this);
        if (this.siguiente != null)
            tmp.addAll(this.siguiente.getListaCondiciones());
        return tmp;
    }

    public Condicion abrir() {
        Condicion condicion = new Condicion();
        condicion.setAbrir(true);
        return concat(condicion);
    }

    public Condicion cerrar() {
        Condicion condicion = new Condicion();
        condicion.setCerrar(true);
        return concat(condicion);
    }

    public Condicion and() {
        Condicion condicion = new Condicion();
        condicion.setNombre("and");
        condicion.setToken_and(true);
        return concat(condicion);
    }

    public Condicion or() {
        Condicion condicion = new Condicion();
        condicion.setNombre("or");
        condicion.setToken_or(true);
        return concat(condicion);
    }

    public Condicion concat(Condicion condicion) {
        if (this.siguiente == null) {
            setSiguiente(condicion);
        } else {
            getSiguiente().concat(condicion);
        }
        return this;
    }

    public Condicion and(Condicion condicion) {
        return and().concat(condicion);
    }

    public Condicion or(Condicion condicion) {
        return or().concat(condicion);
    }

    public boolean contieneCondicion(Condicion condicion) {
        if (condicion != null && condicion.equals(this))
            return true;
        if (this.siguiente == null)
            return false;
        return getSiguiente().contieneCondicion(condicion);
    }

    public void setSiguiente(Condicion siguiente) {
        this.siguiente = siguiente;
    }

    public void obtenerParametros(Parametro parametro) {
        if (this.valor1 != null)
            // switch (this.campo.getTipo()) {
            switch (this.valor1.getTipo()) {
                case Campo.STRING:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorString());
                    break;
                case Campo.BOLEANO:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorBoolean());
                    break;
                case Campo.FECHA:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorFecha());
                    break;
                case Campo.NUMERICO:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorNumerico());
                    break;
                case Campo.INTEGER:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorInteger());
                    break;
                case Campo.LONG:
                    parametro.agregar(this.valor1.getNombre(), this.valor1.getValorLong());
                    break;
            }
        if (this.valor2 != null)
            // switch (this.campo.getTipo()) {
            switch (this.valor2.getTipo()) {
                case Campo.STRING:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorString());
                    break;
                case Campo.BOLEANO:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorBoolean());
                    break;
                case Campo.FECHA:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorFecha());
                    break;
                case Campo.NUMERICO:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorNumerico());
                    break;
                case Campo.INTEGER:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorInteger());
                    break;
                case Campo.LONG:
                    parametro.agregar(this.valor2.getNombre(), this.valor2.getValorLong());
                    break;
            }
        if (this.siguiente != null)
            this.siguiente.obtenerParametros(parametro);
    }

    public String buildQuery() {
        try {
            StringBuilder salida = new StringBuilder("");
            if (this.abrir) {
                salida.append("(");
            } else if (this.cerrar) {
                salida.append(")");
            } else if (this.token_and) {
                salida.append(" and ");
            } else if (this.token_or) {
                salida.append(" or ");
            } else {
                int nElements = 1;
                salida.append(" (");
                if (this.campo != null)
                    nElements = (this.campo.getNombreJPA().split(",")).length;
                for (int i = 0; i < nElements; i++) {
                    if (this.funcion != null)
                        salida.append(this.funcion.getAntesCampo());
                    if (this.campo != null)
                        salida.append(this.campo.getNombreJPA().split(",")[i].trim()).append(" ");
                    if (this.funcion != null) {
                        salida.append(this.funcion.getDespuesCampo());
                        salida.append(this.funcion.getNombreJPA()).append(" ");
                    }
                    if (this.valor1 != null)
                        salida.append(":").append(this.valor1.getNombre());
                    if (this.valor2 != null) {
                        salida.append(" and ");
                        salida.append(":").append(this.valor2.getNombre());
                    }
                    if (i < nElements - 1)
                        salida.append(" or ");
                }
                salida.append(") ");
            }
            if (this.siguiente != null)
                salida.append(this.siguiente.buildQuery());
            return salida.toString();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public String getPruebas() {
        return "pruebas - " + getNombre();
    }

    public String getNombre() {
        if (this.nombre == null || this.nombre.isEmpty()) {
            if (this.abrir)
                return "abrir";
            if (this.cerrar)
                return "cerrar";
            if (this.token_and)
                return "and";
            if (this.token_or)
                return "or";
            return this.campo.getNombre().concat(" ").concat(this.funcion.getNombre()).concat(" ")
                    .concat((this.valor1 != null && this.valor1.getValor() != null) ? this.valor1.getValor().toString()
                            : "")
                    .concat((this.valor2 != null && this.valor2.getValor() != null) ? this.valor2.getValor().toString()
                            : "");
        }
        return this.nombre;
    }

    public String toString() {
        return "Condicion{campo=" + this.campo + ", funcion=" + this.funcion + ", valor1=" + this.valor1 + ", valor2="
                + this.valor2 + ", siguiente=" + this.siguiente + ", abrir=" + this.abrir + ", cerrar=" + this.cerrar
                + ", token_and=" + this.token_and + ", token_or=" + this.token_or + ", nombre=" + this.nombre
                + ", exclusivo=" + this.exclusivo + "}";
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.campo);
        hash = 41 * hash + Objects.hashCode(this.funcion);
        hash = 41 * hash + Objects.hashCode(this.valor1);
        hash = 41 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Condicion other = (Condicion) obj;
        if (!Objects.equals(this.nombre, other.nombre))
            return false;
        if (!Objects.equals(this.campo, other.campo))
            return false;
        if (!Objects.equals(this.funcion, other.funcion))
            return false;
        return Objects.equals(this.valor1, other.valor1);
    }

    public Condicion clonar() {
        Condicion nuevo = new Condicion();
        nuevo.setAbrir(this.abrir);
        nuevo.setCerrar(this.cerrar);
        nuevo.setNombre(this.nombre);
        nuevo.setCampo(this.campo);
        nuevo.setFuncion(this.funcion);
        nuevo.setToken_and(this.token_and);
        nuevo.setToken_or(this.token_or);
        nuevo.setValor1(this.valor1);
        nuevo.setValor2(this.valor2);
        nuevo.setExclusivo(this.exclusivo);
        if (this.siguiente != null)
            nuevo.setSiguiente(this.siguiente.clonar());
        return nuevo;
    }

    public Condicion exclusivo() {
        this.exclusivo = true;
        return this;
    }

    public static class Builder implements QoopoBuilder<Condicion> {
        private String nombre = null;

        private Campo campo;

        private Funcion funcion;

        private Valor valor1;

        private Valor valor2;

        private Condicion siguiente;

        private boolean exclusivo;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder campo(Campo campo) {
            this.campo = campo;
            return this;
        }

        public Builder funcion(Funcion funcion) {
            this.funcion = funcion;
            return this;
        }

        public Builder valor(Valor valor1) {
            this.valor1 = valor1;
            return this;
        }

        public Builder valor2(Valor valor2) {
            this.valor2 = valor2;
            return this;
        }

        public Builder siguiente(Condicion siguiente) {
            this.siguiente = siguiente;
            return this;
        }

        public Builder exclusivo(boolean exclusivo) {
            this.exclusivo = exclusivo;
            return this;
        }

        public Condicion build() {
            Condicion c = new Condicion(this.campo, this.funcion, this.valor1, this.valor2, this.siguiente);
            c.setNombre(this.nombre);
            c.setExclusivo(this.exclusivo);
            return c;
        }
    }
}

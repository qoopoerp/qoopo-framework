package net.qoopo.framework.web.components.kanban;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.jpa.core.interfaces.Graficable;
import net.qoopo.framework.models.OpcionBase;

/**
 * DTO que representa una columna en la vista de kanban
 *
 * @author alberto
 */
@Getter
@Setter
public class KanbanColumn<S, T> implements Serializable {

    private String nombre;
    private S objeto;
    private List<T> items = new ArrayList<>();
    private BigDecimal total;
    private boolean permitirDrag = true;
    private String icon;

    public KanbanColumn(S objeto, String nombre) {
        this.objeto = objeto;
        this.nombre = nombre;
    }

    public KanbanColumn(S objeto, String nombre, String icon) {
        this.objeto = objeto;
        this.nombre = nombre;
        this.icon = icon;
    }

    public KanbanColumn(S objeto, String nombre, String icon, boolean permitirDrag) {
        this.objeto = objeto;
        this.nombre = nombre;
        this.icon = icon;
        this.permitirDrag = permitirDrag;
    }

    public BigDecimal getTotal() {
        total = BigDecimal.ZERO;
        try {
            for (T item : this.items) {
                if (item instanceof Graficable) {
                    total = total.add(((Graficable) item).getGrupoValor(OpcionBase.DEFAULT, OpcionBase.DEFAULT, nombre,
                            null, null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

}

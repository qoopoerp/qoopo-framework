package net.qoopo.framework.jpa.core;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import net.qoopo.framework.exporter.Exportable;
import net.qoopo.framework.exporter.Importable;
import net.qoopo.framework.jpa.core.interfaces.Archivable;
import net.qoopo.framework.jpa.core.interfaces.Duplicable;
import net.qoopo.framework.jpa.core.interfaces.EntityId;
import net.qoopo.framework.jpa.core.interfaces.Ordenable;

@MappedSuperclass
public abstract class AbstractEntity
        implements Serializable, EntityId, Exportable, Importable, Cloneable, Duplicable, Ordenable, Archivable {

    @Column(name = "orderId")
    private Integer order;

    @Column(name = "archived")
    private Boolean archived = Boolean.FALSE;

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Boolean getArchived() {
        return archived;
    }

    @Override
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    @Override
    public int compareTo(Ordenable t) {
        if (getOrder() != null && t != null && t.getOrder() != null) {
            return getOrder().compareTo(t.getOrder());
        }
        return 0;
    }

}

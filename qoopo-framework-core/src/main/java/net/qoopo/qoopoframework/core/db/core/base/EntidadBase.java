package net.qoopo.qoopoframework.core.db.core.base;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import net.qoopo.qoopo.exporter.core.interfaces.Exportable;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.Archivable;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.Duplicable;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.EntidadId;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.Ordenable;

@MappedSuperclass
public abstract class EntidadBase
        implements Serializable, EntidadId, Exportable, Cloneable, Duplicable, Ordenable, Archivable {

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

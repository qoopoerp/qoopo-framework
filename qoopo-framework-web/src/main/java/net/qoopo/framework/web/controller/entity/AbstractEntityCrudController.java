package net.qoopo.framework.web.controller.entity;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.web.controller.AbstractCrudController;

/**
 * Controlador curd para Entities
 */
@Getter
@Setter
public abstract class AbstractEntityCrudController<Entity extends AbstractEntity, EntityID>
        extends AbstractCrudController<Entity, Entity, EntityID> {

    public void loadData() {
        data = repository.findAll();
    }

}

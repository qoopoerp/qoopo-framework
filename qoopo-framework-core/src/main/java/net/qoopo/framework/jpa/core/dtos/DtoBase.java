package net.qoopo.framework.jpa.core.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.jpa.core.AbstractEntity;

/**
 * Representa un DTO base para una entidad.
 */
// @MappedSuperclass
@Getter
@Setter
public abstract class DtoBase extends AbstractEntity {

    @Column(name = "id")
    private Long id;

    private AbstractEntity entity;

}

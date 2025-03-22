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
public abstract class AbstractDto extends AbstractEntity {

    @Column(name = "id")
    private Long id;

    // @JsonIgnore
    private AbstractEntity entity;

}

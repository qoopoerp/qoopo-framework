package net.qoopo.framework.jpa.core.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.jpa.core.EntidadBase;

/**
 * Representa un DTO base para una entidad.
 */
// @MappedSuperclass
@Getter
@Setter
public abstract class DtoBase extends EntidadBase {

    @Column(name = "id")
    private Long id;

    private EntidadBase entity;

}

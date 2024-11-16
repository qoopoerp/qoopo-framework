package net.qoopo.framework.web.core.dto.lazy;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.data.repository.CrudRepository;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.dtos.DtoBase;
import net.qoopo.framework.jpa.filter.Filter;
import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.models.OpcionBase;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Clase de esqueleto de los beans de administración
 *
 * @author alberto
 * @param <DTO>
 * @param <T>
 */
@Getter
@Setter
public abstract class AbstractLazyAdminDtoDetailController<R extends AbstractEntity, DTO extends DtoBase, T>
        extends AbstractLazyAdminDtoController<R, DTO> {

    public AbstractLazyAdminDtoDetailController(String entityClassName, Class<R> entityClass, Filter inicial,
            List<Condicion> condicionesDisponibles,
            List<Campo> campos, List<OpcionBase> opcionesGrupos) {
        super(entityClassName, entityClass, inicial, condicionesDisponibles, campos, opcionesGrupos);
    }

    protected CrudRepository<T, Long> repositoryDetalle;

    protected T itemDetalle;
    protected boolean editandoDetalle;
    protected List<T> listaEliminar = new ArrayList<>();

    public abstract void nuevoDetalle();

    public abstract void agregarDetalle();

    public abstract void eliminarDetalle(T item);

    @Override
    public void update() {
        try {
            for (T det : listaEliminar) {
                repositoryDetalle.delete(det);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(languageProvider.getTextValue(20) + e.getMessage());
        }
        super.update();
    }

    @Override
    public void edit(DTO item) {
        super.edit(item); // To change body of generated methods, choose Tools | Templates.
        listaEliminar.clear();
    }

}

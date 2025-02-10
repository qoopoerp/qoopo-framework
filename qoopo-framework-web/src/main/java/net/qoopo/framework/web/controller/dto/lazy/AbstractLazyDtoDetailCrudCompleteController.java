package net.qoopo.framework.web.controller.dto.lazy;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.data.repository.CrudRepository;
import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.jpa.core.AbstractEntity;
import net.qoopo.framework.jpa.core.dtos.DtoBase;
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
public abstract class AbstractLazyDtoDetailCrudCompleteController<R extends AbstractEntity, DTO extends DtoBase, T>
        extends AbstractLazyDtoCrudCompleteController<R, DTO> {

    public AbstractLazyDtoDetailCrudCompleteController(String entityClassName, Class<DTO> entityClass, Filter inicial,
            List<Condition> condicionesDisponibles,
            List<Field> campos, List<OpcionBase> opcionesGrupos) {
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
            e.printStackTrace();
        }
        super.update();
    }

    @Override
    public void edit(DTO item) {
        super.edit(item);
        listaEliminar.clear();
    }

}

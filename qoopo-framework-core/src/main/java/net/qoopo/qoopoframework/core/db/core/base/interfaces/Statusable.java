package net.qoopo.qoopoframework.core.db.core.base.interfaces;

import java.util.List;

import net.qoopo.qoopoframework.core.db.core.base.dtos.Estado;

/**
 * Esta interfaz es para las entidades que deben tener estado.
 *
 * @author alberto
 */
public interface Statusable {

    public int getEstado();

    public void setEstado(int estado);

    public void setEstado(Estado estado);

    public String getEstadoTexto();

    // public String getEstadoColor();

    public List<Estado> getEstados();

}

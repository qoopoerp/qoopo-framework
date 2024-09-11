package net.qoopo.qoopoframework.core.db.core.base.interfaces;

import java.math.BigDecimal;
import java.util.List;

import net.qoopo.qoopoframework.core.db.core.base.dtos.base.OpcionBase;

/**
 * Esta interfaz es para las entidades que deben ser graficables.
 *
 * @author alberto
 */
public interface Graficable {

    public static final OpcionBase UNIDADES = new OpcionBase(-1, 1588, "Cuenta");

    /**
     * Devuelve las opciones de metricas para obtener el valor
     *
     * @return
     */
    public List<OpcionBase> getOpcionesMetricas();

    /**
     * Devuelve las opciones de grupos
     *
     * @return
     */
    public List<OpcionBase> getOpcionesGrupos();

    /**
     * Obtiene el grupo o clave para mostrar el grafico. Puede ser varios en
     * caso de agrupar por el detalel en maestor detalle
     *
     * @param opcion
     * @return
     */
    public List<String> getGrupo(OpcionBase opcion);

    /**
     * Obtiene el valor del grafico en funcion de la opcion
     * 
     * @param opcionMetrica    La opcion de metrica a obtener
     * @param opcionGrupo      la opcion de grupo seleccionada, util cuando se
     *                         selecciona un grupo q se obtiene a nivel de detalle
     *                         en
     *                         Maestro-detalle
     * @param grupo            El valor del grupo para discriminar el valor
     * 
     * @param opcionGrupoPadre la opcion de grupo seleccionada,
     *                         util cuando se
     *                         selecciona un grupo q se obtiene a nivel de detalle
     *                         en
     *                         Maestro-detalle
     * @param grupoPadre       El valor del grupo para discriminar el valor
     * @return
     */
    public BigDecimal getGrupoValor(OpcionBase opcionMetrica, OpcionBase opcionGrupo, String grupo,
            OpcionBase opcionGrupoPadre, String grupoPadre);
}

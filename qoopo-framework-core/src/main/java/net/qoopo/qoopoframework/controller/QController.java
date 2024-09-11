package net.qoopo.qoopoframework.controller;

import java.util.List;

/**
 * En Qoopo, Un controller es un servicio que provee cualquier funcionalidad y
 * que desee ser desacoplada. Las clases utilitarias, servicios, de negocio o de
 * acceso a la base de datos que deseen estar desacopladas de otras para
 * permitir la modularidad, deben implementar QController.
 *
 * Para llamar a un controlador (servicio, clase utilitaria, dao) llamar al
 * metodo QControllerManager.get(identificador), donde identificador puede ser
 * la ruta de la clase (ruta paquetes/Clase) o el nombre del controller que
 * devuelve con el metodo getName();
 *
 * @author alberto
 */
//net.qoopo.qoopo.controller.QController
public interface QController {

    public String getName();

    public void instanciar(Object... parametros);

    public void set(String opcion, Object valor);

    public Object get(String opcion, Object... parametros);

    /**
     * Ejecuta un metodo del controlador
     *
     * @param opcion
     * @param parametros
     * @return
     * @throws Exception
     */
    public Object run(String opcion, Object... parametros) throws Exception;

//    public Object runObserver(String opcion, Object... parametros) throws Exception;
    /**
     * Notifica a los observadores que se registraron en un evento
     *
     * @param event
     * @param parametros
     * @return
     * @throws Exception
     */
    public Object notifyObserver(String event, Object... parametros) throws Exception;

    /**
     * Devuelve la lista de los eventos a los que se va a suscribrir
     *
     * @return
     */
    public List<String> getListSusbriptions();

}

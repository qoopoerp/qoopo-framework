package net.qoopo.qoopoframework.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Administrador de los controller registrados en Qoopo-framework
 *
 * @author alberto
 */
public final class QControllerManager {

    public static final Logger log = Logger.getLogger("Qoopo");
    private static final List<QController> INSTANCES = new ArrayList<>();
    private static final Map<String, List<QController>> observers = new HashMap<>();

    public static void load() {
        try {
            INSTANCES.clear();
            observers.clear();
            ServiceLoader<QController> cargados = ServiceLoader.load(QController.class);
            cargados.forEach(controller -> {
                log.info("[+] Controller cargado: [".concat(controller.getName()).concat("] ")
                        .concat(controller.getClass().getName()));
                // suscribmos observers
                List<String> listObservers = controller.getListSusbriptions();
                if (listObservers != null && !listObservers.isEmpty()) {
                    for (String event : controller.getListSusbriptions()) {
                        if (observers.containsKey(event)) {
                            List<QController> tmp = observers.get(event);
                            if (tmp == null) {
                                tmp = new ArrayList<>();
                            }
                            tmp.add(controller);
                            observers.put(event, tmp);
                        } else {
                            List<QController> tmp = new ArrayList<>();
                            tmp.add(controller);
                            observers.put(event, tmp);
                        }
                    }
                }
                INSTANCES.add(controller);
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Devuelve el controllador con el nombre name.
     *
     * @param name nombre del controllador. Tambien puede indicar la ruta
     *             completa de la clase
     * @return
     */
    public static QController get(String name) {
        try {
            for (QController controller : INSTANCES) {
                if (controller.getName().equals(name)) {
                    return controller;
                }
            }
            // si no se encuentra, se intenta crear una nueva instanacia
            log.log(Level.WARNING, "SE INTENTA INSTANCIAR CONTROLLER PARA LA CLASE [{0}]", name);
            return ((QController) Class.forName(name).newInstance());
        } catch (Exception e) {
            log.log(Level.SEVERE, name, e);
        }
        return null;
    }

    /**
     * Ejecuta un metodo del controlador
     *
     * @param name
     * @param opcion
     * @param parametros
     * @return
     */
    public static Object run(String name, String opcion, Object... parametros) {
        try {
            QController tmp = get(name);
            if (tmp != null) {
                return tmp.run(opcion, parametros);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Notifica a los observadores que se registraron en un evento
     *
     * @param event
     * @param parametros
     */
    public static void notifyObserver(String event, Object... parametros) {
        try {
            List<QController> tmp = observers.get(event);
            if (tmp != null && !tmp.isEmpty()) {
                tmp.forEach(q -> {
                    try {
                        q.notifyObserver(event, parametros);
                    } catch (Exception ex) {
                        Logger.getLogger(QControllerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }
}

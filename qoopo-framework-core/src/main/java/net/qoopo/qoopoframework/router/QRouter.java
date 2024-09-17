package net.qoopo.qoopoframework.router;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador que se encarga de manejar las rutas de navegacion del aplicativo.
 * Se deben registrar las rutas a las que se permite navegar. Por cada ruta se
 * debe indicar el tipo de acceso
 *
 * 0 Publico. 1 Sitio Web 2 Backend Se debe indicar el debe existir sesion o no
 * para cada ruta
 *
 * @author Alberto
 */
public class QRouter {

    public static final Logger log = Logger.getLogger("Qoopo");
    public static final List<QRoute> ROUTES = new ArrayList<>();

    static {
      
        // webservices
        // if (urlStr.contains("/mobil/")) {
        // // System.out.println("contiene parte mobil, no debe proteger");
        // return 2; //proteccion mobil
        // }
    }

    public static void register(QRoute route) {
        try {
            if (!ROUTES.contains(route)) {
                ROUTES.add(route);
                log.log(Level.INFO, "[+] Route registered [{0}]", route.getRoute());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, null, e);
        }
    }

    public static void unRegister(QRoute route) {
        try {
            if (ROUTES.contains(route)) {
                ROUTES.remove(route);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, null, e);
        }
    }

    /**
     * Valida una ruta e identifica el tipo de proteccion que necesita
     *
     * @param route
     * @return Type of @QRoute.TYPES
     */
    public static QRoute validateRoute(String route) {
        route = route.toLowerCase();
        for (QRoute _route : ROUTES) {
            if (_route.isExactRoute()) {
                if (route.equalsIgnoreCase(_route.getRoute().toLowerCase()) || (_route.getPage() != null
                        && !_route.getPage().isBlank() && route.equalsIgnoreCase(_route.getPage().toLowerCase()))) {
                    return _route;
                }
            } else {
                if (route.contains(_route.getRoute().toLowerCase()) || (_route.getPage() != null
                        && !_route.getPage().isBlank() && route.contains(_route.getPage().toLowerCase()))) {
                    return _route;
                }
            }
        }
        return QRoute.INVALID_ROUTE;
    }
}

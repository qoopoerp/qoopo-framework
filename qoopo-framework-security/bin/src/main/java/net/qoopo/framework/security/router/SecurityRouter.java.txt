package net.qoopo.framework.security.router;

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
public class SecurityRouter {

    public static final Logger log = Logger.getLogger("Qoopo");
    public static final List<SecurityRoute> ROUTES = new ArrayList<>();

    static {

        // webservices
        // if (urlStr.contains("/mobil/")) {
        // // System.out.println("contiene parte mobil, no debe proteger");
        // return 2; //proteccion mobil
        // }
    }

    public static void register(SecurityRoute route) {
        try {
            if (!ROUTES.contains(route)) {
                ROUTES.add(route);
                log.log(Level.INFO, "[+] Route registered [{0}]", route.getRoute());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, null, e);
        }
    }

    public static void unRegister(SecurityRoute route) {
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
    public static SecurityRoute validateRoute(String route) {
        return validateRoute(route, ROUTES);
    }

    public static SecurityRoute validateRoute(String route, List<SecurityRoute> routes) {
        route = route.toLowerCase();
        for (SecurityRoute registeredRoute : routes) {
            if (registeredRoute.isExactRoute()) {
                if (route.equalsIgnoreCase(registeredRoute.getRoute().toLowerCase())
                        || (registeredRoute.getPage() != null
                                && !registeredRoute.getPage().isBlank()
                                && route.equalsIgnoreCase(registeredRoute.getPage().toLowerCase()))) {
                    return registeredRoute;
                }
            } else {
                if (route.contains(registeredRoute.getRoute().toLowerCase()) || (registeredRoute.getPage() != null
                        && !registeredRoute.getPage().isBlank()
                        && route.contains(registeredRoute.getPage().toLowerCase()))) {
                    return registeredRoute;
                }
            }
        }
        return SecurityRoute.INVALID_ROUTE;
    }
}

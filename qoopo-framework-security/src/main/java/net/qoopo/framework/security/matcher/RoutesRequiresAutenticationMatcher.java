package net.qoopo.framework.security.matcher;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.SecurityConfigRouter;
import net.qoopo.framework.security.router.SecurityRoute;
import net.qoopo.framework.security.router.SecurityRouter;

/**
 * Valida si una ruta registrda en el router corresponde a un request en
 * particular
 */
public class RoutesRequiresAutenticationMatcher implements RequestMatcher {

    private static Logger log = Logger.getLogger("autenticacion");

    @Override
    public boolean match(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        log.info("validando " + urlStr);
        // SecurityRoute route = SecurityRouter.validateRoute(urlStr.toLowerCase(),
        // SecurityConfigRouter.get().getRequireAuthentication());

        SecurityRoute route = SecurityRouter.validateRoute(urlStr.toLowerCase());
        log.info("result-> " + route);

        return route != null && route.getType() != SecurityRoute.TYPE_INVALID && route.isRequireSession();
    }

}

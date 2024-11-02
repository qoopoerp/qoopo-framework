package net.qoopo.framework.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import net.qoopo.framework.security.router.SecurityRoute;
import net.qoopo.framework.security.router.SecurityRouter;

/**
 * Valida si una ruta registrda en el router corresponde a un request en
 * particular
 */
public class RoutesDontRequiresAuthenticacionMatcher implements RequestMatcher {

    @Override
    public boolean match(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        // SecurityRoute route = SecurityRouter.validateRoute(urlStr.toLowerCase(),
        // SecurityConfigRouter.get().getDontRequireAuthentication());
        SecurityRoute route = SecurityRouter.validateRoute(urlStr.toLowerCase());
        return route != null && route.getType() != SecurityRoute.TYPE_INVALID && !route.isRequireSession();
    }

}

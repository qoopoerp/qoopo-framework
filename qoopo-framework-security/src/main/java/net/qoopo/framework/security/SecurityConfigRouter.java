package net.qoopo.framework.security;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.qoopo.framework.security.router.SecurityRoute;
import net.qoopo.framework.security.router.SecurityRouter;

public class SecurityConfigRouter {

    private static SecurityConfigRouter INSTANCE = null;

    private List<SecurityRoute> routes = new ArrayList<>();

    @Getter
    private List<SecurityRoute> requireAuthentication = new ArrayList<>();
    @Getter
    private List<SecurityRoute> dontRequireAuthentication = new ArrayList<>();

    public static SecurityConfigRouter get() {
        if (INSTANCE == null)
            INSTANCE = new SecurityConfigRouter();
        return INSTANCE;
    }

    public SecurityRoute configRoute(String pathMatch) {
        SecurityRoute route = SecurityRoute.builder()
                .page(pathMatch.replace("*", ""))
                .route(pathMatch.replace("*", ""))
                .exactRoute(!pathMatch.contains("*"))
                .requirePermission(true)
                .requireSession(true)
                .type(SecurityRoute.TYPE_BACKEND)
                .build();
        routes.add(route);
        return route;
    }

    public SecurityConfigRouter config() {
        routes.forEach(c -> {
            SecurityRouter.register(c);
            if (c.isRequireSession()) {
                requireAuthentication.add(c);
                System.out.println("agregando como valid " + c);
            } else {
                dontRequireAuthentication.add(c);
                System.out.println("agregando como invalid " + c);
            }
        });
        // routes.stream().filter(c -> c.isRequireSession()).map(c ->
        // requireAuthentication.add(c));
        // routes.stream().filter(c -> !c.isRequireSession()).map(c ->
        // dontRequireAuthentication.add(c));
        return this;
    }

}
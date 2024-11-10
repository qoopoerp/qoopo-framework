package net.qoopo.framework.security.context.repository;

import net.qoopo.framework.security.context.SecurityContext;
import net.qoopo.framework.security.web.repository.HttpRepository;
import net.qoopo.framework.security.web.repository.HttpWarehouse;

/**
 * Repositorio para almacenar el security context en un HttpWarehouse
 */

public class SecurityContextHttpRepository extends HttpRepository<SecurityContext>
        implements SecurityContextRepository<HttpWarehouse> {

    private static final String httpAttributeName = "__qoopo_securityc";

    public SecurityContextHttpRepository() {
        super(httpAttributeName);
    }

    public SecurityContextHttpRepository(boolean allowCreateSession) {
        super(httpAttributeName, allowCreateSession);
    }
}

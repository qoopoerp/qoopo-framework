package net.qoopo.framework.security.core.context.repository;

import net.qoopo.framework.security.core.context.SecurityContext;
/**
 * Almacena el securityContext en un almacen definido por T
 */
public interface SecurityContextRepository<T> {
    public void save(SecurityContext context,T warehouse);

    public SecurityContext load(T warehouse);
}

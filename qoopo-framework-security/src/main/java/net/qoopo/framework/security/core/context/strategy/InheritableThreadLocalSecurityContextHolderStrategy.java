package net.qoopo.framework.security.core.context.strategy;

import net.qoopo.framework.security.core.context.SecurityContext;
import net.qoopo.framework.security.core.context.SecurityContextImpl;

/**
 * Almacena en un ThreadLocal
 */
public class InheritableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private static final ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<>();

    @Override
    public void clear() {
        contextHolder.remove();
    }

    @Override
    public SecurityContext getContext() {
        SecurityContext result = contextHolder.get();
        if (result == null) {
             result = createEmptyContext();
            contextHolder.set(result);
        }
        return result;
    }

    @Override
    public void setContext(SecurityContext context) {
        contextHolder.set( context);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

}

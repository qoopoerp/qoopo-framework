package net.qoopo.framework.security.context.strategy;

import net.qoopo.framework.security.context.SecurityContext;
import net.qoopo.framework.security.context.SecurityContextImpl;

/**
 * Almacena en un ThreadLocal
 */
public class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

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
        contextHolder.set(context);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

}
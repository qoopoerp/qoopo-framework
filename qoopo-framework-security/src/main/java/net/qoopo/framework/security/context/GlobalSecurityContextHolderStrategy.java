package net.qoopo.framework.security.context;

/**
 * Almacena un solo SecurityContext para toda la aplicación
 */
public class GlobalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private static SecurityContext context;

    @Override
    public void clear() {
        context = null;
    }

    @Override
    public SecurityContext getContext() {
        if (context == null) {
            context = new SecurityContextImpl();
        }
        return context;
    }

    @Override
    public void setContext(SecurityContext context) {
        GlobalSecurityContextHolderStrategy.context=context;
    }

    @Override
    public SecurityContext createEmptyContext() {
        context= new SecurityContextImpl();
        return context;
    }

}
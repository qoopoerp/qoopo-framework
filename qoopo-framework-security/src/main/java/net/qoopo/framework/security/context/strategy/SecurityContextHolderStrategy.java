package net.qoopo.framework.security.context.strategy;

import net.qoopo.framework.security.context.SecurityContext;

/**
 * Una estategía define como se almacena un SecurityContext
 */
public interface SecurityContextHolderStrategy {
    public void clear();

    public SecurityContext getContext();

    public void setContext(SecurityContext context);

    public SecurityContext createEmptyContext();
}
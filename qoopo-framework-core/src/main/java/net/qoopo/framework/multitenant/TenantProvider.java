package net.qoopo.framework.multitenant;

/**
 * Devuelve el actual identificador del Tenant
 */
public interface TenantProvider {

    public Long getTenantId();
}

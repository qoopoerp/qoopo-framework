package net.qoopo.framework.multitenant;

import net.qoopo.framework.jpa.filter.condicion.Campo;
import net.qoopo.framework.jpa.filter.condicion.Condicion;
import net.qoopo.framework.jpa.filter.condicion.Funcion;
import net.qoopo.framework.jpa.filter.condicion.Valor;

public class MultitenantFilter {

    /**
     * El campo que identifica el Tenant
     */
    public static Campo TENANT_ID = new Campo(Campo.LONG, "Empresa", "o.empresa.id");

    public static Condicion tenantCondition(Long tenantId) {
        return Condicion.getBuilder().campo(TENANT_ID).funcion(Funcion.IGUAL)
                .valor(new Valor("tenantId", tenantId)).build();
    }

}

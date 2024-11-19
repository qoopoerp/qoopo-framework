package net.qoopo.framework.multitenant;

import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Function;
import net.qoopo.framework.filter.core.condition.Value;

public class MultitenantFilter {

    /**
     * El Field que identifica el Tenant
     */
    public static Field TENANT_ID = new Field(Field.LONG, "Empresa", "o.empresa.id");

    public static Condition tenantCondition(Long tenantId) {
        return Condition.builder().field(TENANT_ID).function(Function.IGUAL)
                .value(new Value("tenantId", tenantId)).build();
    }

}

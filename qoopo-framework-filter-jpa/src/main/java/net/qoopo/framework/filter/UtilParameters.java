package net.qoopo.framework.filter;

import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.data.jpa.JpaParameters;

public class UtilParameters {
     public static void getJpaParameters(Condition condition, JpaParameters parameters) {
        if (condition.getValue() != null)
            parameters.add(condition.getValue().getName(), condition.getValue().get());

        if (condition.getValue2() != null)
            parameters.add(condition.getValue2().getName(), condition.getValue2().get());

        if (condition.getNext() != null)
            getJpaParameters(condition.getNext(), parameters);
    }

    public static JpaParameters getJpaParameters(Filter filter, JpaParameters parameters) {
        if (filter != null && filter.getCondition() != null) {
            getJpaParameters(filter.getCondition(), parameters);
        }
        return parameters;
    }

}

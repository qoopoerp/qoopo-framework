package net.qoopo.framework.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.filter.core.Filter;
import net.qoopo.framework.filter.core.condition.Condition;
import net.qoopo.framework.filter.core.condition.Field;
import net.qoopo.framework.filter.core.condition.Function;
import net.qoopo.framework.filter.core.condition.Value;

public class ConditionTest {

    private static Logger log = Logger.getLogger("Condition-test");

    @Test
    public void testCondition() {
        try {

            Field f1 = new Field("Nombre", "o.name");
            Field f2 = new Field(Field.FECHA, "Fecha", "o.fecha");

            // Function fIgual
            Condition c1 = Condition.builder().field(f1).function(Function.IGUAL).value(Value.of("Alberto")).build();
            Condition c2 = Condition.builder().field(f2).function(Function.ENTRE).value(Value.of(LocalDateTime.now()))
                    .value2(Value.of(LocalDateTime.now().plusDays(5))).build();
            Condition c3 = c1.clonar().and(c2);

            String query = c1.buildQuery();
            log.info("query=" + query);
            log.info("query 2=" + c2.buildQuery());
            log.info("query 3=" + c3.buildQuery());
            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

}

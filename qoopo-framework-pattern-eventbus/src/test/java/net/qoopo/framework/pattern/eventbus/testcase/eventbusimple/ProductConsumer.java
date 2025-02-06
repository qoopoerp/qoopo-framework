package net.qoopo.framework.pattern.eventbus.testcase.eventbusimple;

import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.Consumer;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

/**
 * consumidor de un producto
 */
@AllArgsConstructor
public class ProductConsumer implements Consumer<String, Product> {

    private static Logger log = Logger.getLogger("ProductConsumer");

    private String message;

    @Override
    public void receive(EventRecord<String, Product> eventRecord) {
        log.info("Recibiendo [" + eventRecord.getKey() + "] " + eventRecord.getValue().getName() + " -> " + message);
    }

}

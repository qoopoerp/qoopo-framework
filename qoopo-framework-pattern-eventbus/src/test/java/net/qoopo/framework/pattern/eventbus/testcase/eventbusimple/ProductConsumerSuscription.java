package net.qoopo.framework.pattern.eventbus.testcase.eventbusimple;

import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.ConsumerSusbscriptor;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

@AllArgsConstructor
public class ProductConsumerSuscription implements ConsumerSusbscriptor<String, Product> {

    private static Logger log = Logger.getLogger("ProductConsumer");

    private String message;

    private EventBus bus;

    @Override
    public void receive(EventRecord<String, Product> eventRecord) {
        log.info("[SUB] Recibiendo [" + eventRecord.getKey() + "] " + eventRecord.getValue().getName() + " -> "
                + message);
    }

    @Override
    public void suscribe(String topic) {
        bus.suscribe(topic, this);
    }

}

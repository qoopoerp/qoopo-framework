package net.qoopo.framework.pattern.eventbus.testcase.eventbusimple;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.producer.Producer;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

@AllArgsConstructor
public class ProductProducer implements Producer<String, Product> {

    private EventBus<String, Product> bus;

    @Override
    public void send(EventRecord<String, Product> record) {
        bus.publish(record.getKey(), record);
    }

}

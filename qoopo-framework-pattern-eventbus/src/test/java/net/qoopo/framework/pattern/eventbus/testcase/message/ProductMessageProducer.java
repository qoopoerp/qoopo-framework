package net.qoopo.framework.pattern.eventbus.testcase.message;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.producer.Producer;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

@AllArgsConstructor
public class ProductMessageProducer implements Producer<String, EventMessage<Product>> {

    private EventBus<String, EventMessage<Product>> bus;

    @Override
    public void send(EventRecord<String, EventMessage<Product>> record) {
        bus.publish(record.getKey(), record);
    }

}

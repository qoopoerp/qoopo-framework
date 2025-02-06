package net.qoopo.framework.pattern.eventbus.testcase.message;

import java.time.Duration;
import java.util.List;

import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.ConsumerPoll;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

public class ProductMessageConsumerPoll implements ConsumerPoll<String, EventMessage<Product>> {

    private EventBus<String, EventMessage<Product>> bus;
    private String topic;

    public ProductMessageConsumerPoll(EventBus<String, EventMessage<Product>> bus, String topic) {
        this.bus = bus;
        this.topic = topic;
    }

    @Override
    public List<EventRecord<String, EventMessage<Product>>> poll(Duration duration) {
        return bus.getEvents(topic);
    }

}

package net.qoopo.framework.pattern.eventbus.message;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.ConsumerPoll;

/**
 * Consumidor de mensajes para un EventBus indicado
 */
public class MessageConsumerPoll<T> implements ConsumerPoll<String, EventMessage<T>> {

    private EventBus<String, EventMessage<T>> bus;
    private String topic;
    private UUID uuid;

    public MessageConsumerPoll(EventBus<String, EventMessage<T>> bus, String topic) {
        this.bus = bus;
        this.topic = topic;
        uuid = UUID.randomUUID();
    }

    @Override
    public List<EventRecord<String, EventMessage<T>>> poll(Duration duration) {
        return bus.getEvents(uuid.toString(), topic, duration);
    }

    public void commitEventReaded() {
        bus.commitEventReaded(uuid.toString(), topic, null);
    }

}

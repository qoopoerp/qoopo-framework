package net.qoopo.framework.pattern.eventbus;

import java.util.List;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.Consumer;

/**
 * Define in Bus de eventos donde se van a registrar los mensajes
 */
public interface EventBus<KEY, VALUE> {

    public void suscribe(String topic, Consumer<KEY, VALUE> consumer);

    public void publish(String topic, EventRecord<KEY, VALUE> eventRecord);

    public List<EventRecord<KEY,VALUE>> getEvents(String topic);

}

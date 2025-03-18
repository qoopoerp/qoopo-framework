package net.qoopo.framework.pattern.eventbus.message;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.producer.Producer;

/**
 * Productor de mensajes para ser usados por Qoopo-framework
 */
@AllArgsConstructor
public class MessageProducer<T> implements Producer<String, T> {

    private EventBus<String, T> bus;

    @Override
    public void send(EventRecord<String, T> record) {
        bus.publish(record.getKey(), record);
    }

}

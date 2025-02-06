package net.qoopo.framework.eventbus.core;

import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.producer.Producer;

/**
 * Productor de mensajes para ser usados por Qoopo-framework
 */
public interface MessageProducer<T> extends Producer<String, EventMessage<T>> {
    
}

package net.qoopo.framework.pattern.eventbus.producer;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;

/**
 * Define un productor
 */
public interface Producer<KEY, VALUE> {

    public void send(EventRecord<KEY, VALUE> record);
}

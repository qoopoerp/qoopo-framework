package net.qoopo.framework.pattern.eventbus.consumer;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;

/**
 * Define un consumidor
 */
public interface Consumer<KEY, VALUE> {

    public void receive(EventRecord<KEY,VALUE> eventRecord);

}

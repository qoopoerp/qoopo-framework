package net.qoopo.framework.pattern.eventbus.consumer;

import java.time.Duration;
import java.util.List;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;

public interface ConsumerPoll<KEY, VALUE> {
    public List<EventRecord<KEY, VALUE>> poll(Duration duration);
}

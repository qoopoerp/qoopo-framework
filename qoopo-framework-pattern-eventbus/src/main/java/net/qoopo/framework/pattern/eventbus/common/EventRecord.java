package net.qoopo.framework.pattern.eventbus.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EventRecord<KEY, VALUE> implements Serializable {
    private KEY key;
    private VALUE value;
}

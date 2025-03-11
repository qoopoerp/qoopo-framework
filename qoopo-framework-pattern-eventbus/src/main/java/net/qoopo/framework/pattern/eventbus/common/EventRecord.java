package net.qoopo.framework.pattern.eventbus.common;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EventRecord<KEY, VALUE> implements Serializable {
    private UUID id;
    private KEY key;
    private VALUE value;

    public EventRecord(KEY key, VALUE value) {
        this.key = key;
        this.value = value;
    }
}

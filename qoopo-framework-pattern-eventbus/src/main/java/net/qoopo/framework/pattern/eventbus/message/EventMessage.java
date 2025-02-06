package net.qoopo.framework.pattern.eventbus.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Data
@ToString
public class EventMessage<T> implements Serializable {
    private MessageHeaders headers;
    private T payload;
}

package net.qoopo.framework.pattern.eventbus.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventMessage<T> implements Serializable {
    protected MessageHeaders headers;
    protected String type = this.getClass().getName();
    protected T payload;

    public EventMessage() {
        headers = null;
        payload = null;
    }

    public EventMessage(MessageHeaders headers, T payload) {
        this.headers = headers;
        this.payload = payload;
    }

}
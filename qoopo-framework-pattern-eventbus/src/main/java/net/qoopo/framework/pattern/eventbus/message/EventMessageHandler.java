package net.qoopo.framework.pattern.eventbus.message;

/**
 * Define un manejador de mensajes
 */
@FunctionalInterface
public interface EventMessageHandler<T> {
    // public void handleMessage(EventMessageT<?> eventMessage);
    public void handleMessage(T eventMessage);
}
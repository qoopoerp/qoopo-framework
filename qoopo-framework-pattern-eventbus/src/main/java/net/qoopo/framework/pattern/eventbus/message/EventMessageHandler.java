package net.qoopo.framework.pattern.eventbus.message;

/**
 * Define un manejador de mensajes
 */
@FunctionalInterface
public interface EventMessageHandler {
    public void handleMessage(EventMessage<?> eventMessage);
}
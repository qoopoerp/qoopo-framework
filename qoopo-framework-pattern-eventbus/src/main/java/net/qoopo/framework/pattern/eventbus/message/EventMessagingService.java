package net.qoopo.framework.pattern.eventbus.message;

/**
 * Servicio de envío y recepción de mensajes
 */
public interface EventMessagingService<T> {
    public void sendEvent(String destination, EventMessage<T> message);

    public void receiveEvents(String destination, EventMessageHandler handler);

    public String getStatus();
}

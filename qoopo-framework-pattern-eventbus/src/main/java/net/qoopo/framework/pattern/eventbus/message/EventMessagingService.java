package net.qoopo.framework.pattern.eventbus.message;

/**
 * Servicio de envío y recepción de mensajes
 */
public interface EventMessagingService<T> {

    public void sendEvent(String destination, T message);

    public void receiveEvents(String destination, EventMessageHandler<T> handler);

    public String getStatus();
}

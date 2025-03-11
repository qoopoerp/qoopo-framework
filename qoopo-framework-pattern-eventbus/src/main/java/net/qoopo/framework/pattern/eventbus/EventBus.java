package net.qoopo.framework.pattern.eventbus;

import java.time.Duration;
import java.util.List;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.Consumer;

/**
 * Define un Bus de eventos donde se van a registrar los mensajes
 */
public interface EventBus<KEY, VALUE> {

    /**
     * Suscribe un consumidor para ser notificado cuadno haya un nuevo mensaje
     * 
     * @param topic
     * @param consumer
     */
    public void suscribe(String topic, Consumer<KEY, VALUE> consumer);

    /**
     * Publica un mensaje en una cola/topic
     * 
     * @param topic
     * @param eventRecord
     */
    public void publish(String topic, EventRecord<KEY, VALUE> eventRecord);

    /**
     * Obtiene los eventos que se encuentren en en una cola
     * 
     * @param consumerId
     * @param topic
     * @param duration
     * @return
     */
    public List<EventRecord<KEY, VALUE>> getEvents(String consumerId, String topic, Duration duration);

    /**
     * Confirma la lectura de un evento
     * 
     * @param consumerId
     * @param topic
     * @param eventUuid
     */
    public void commitEventReaded(String consumerId, String topic, String eventUuid);

    public String getStatus();

}

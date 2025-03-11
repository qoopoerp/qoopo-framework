package net.qoopo.framework.pattern.eventbus.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.Consumer;

/**
 * Implementación de un Eventbus en memoria
 * 
 * Soport dos tipos de Uso:
 * 
 * 1.- Tipo cola en el cual se publica un mensaje a una cola tipo Kafka en la
 * cual se pregunta al bus cada cierto tiempo si hay mensajes en una cola
 * 
 * 2. Tipo susbscriptor en el cual se le notifica a los Consumidores que están
 * suscriptos
 * un Consumidor debe implementar <<Consumer>>.
 * Útil para manejar eventos dentro de la misma aplicación
 */
@Getter
@Setter
public class EventBusMemoryImpl<KEY, VALUE> implements EventBus<KEY, VALUE> {

    private static Logger log = Logger.getLogger("event-bus-memory-impl");
    // la cola de mensajes para los que aún no se han suscrito en el modelo de Kafa
    // donde se pide si hay mensajes en un topic
    private Map<String, List<EventRecord<KEY, VALUE>>> queue;

    // la lista de suscriptores en el modelo de suscriptores registrados
    private Map<String, List<Consumer<KEY, VALUE>>> suscriptors;

    // Mantiene un contador de offset por cada consumidor y cada cola,
    private Map<String, Map<String, Integer>> offsetMap;
    private Map<String, Map<String, Integer>> offsetSizeMap;
    private boolean debug = false;

    public EventBusMemoryImpl() {
        queue = new HashMap<>();
        suscriptors = new HashMap<>();
        offsetMap = new HashMap<>();
        offsetSizeMap = new HashMap<>();
        log.warning("[!!] Iniciando un Bus de Eventos en Memoria.");
        log.warning("[!!] Úselo solo en entornos no productivos");
    }

    @Override
    public void suscribe(String topic, Consumer<KEY, VALUE> consumer) {
        if (!suscriptors.containsKey(topic))
            suscriptors.put(topic, new ArrayList<>());
        suscriptors.get(topic).add(consumer);
    }

    @Override
    public void publish(String topic, EventRecord<KEY, VALUE> eventRecord) {
        // agrega el mensaje en la cola para el modelo sin suscripcion
        try {
            if (!queue.containsKey(topic) || queue.get(topic) == null)
                queue.put(topic, new ArrayList<>());

            if (debug)
                log.warning("Adding to Topic " + topic);
            eventRecord.setId(UUID.randomUUID());
            queue.get(topic).add(eventRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // notifica a los suscritos
        if (suscriptors.containsKey(topic)) {
            log.warning("notifying to subscriptors " + topic);
            suscriptors.get(topic).stream().parallel().forEach(c -> c.receive(eventRecord));
        }
    }

    @Override
    public List<EventRecord<KEY, VALUE>> getEvents(String consumerId, String topic, Duration duration) {
        try {
            var current = queue.get(topic);
            if (current == null)
                return null;
            var offset = getOffset(consumerId, topic);
            var subList = current.subList(offset, current.size());
            offsetSizeMap.get(consumerId).put(topic, subList.size());
            if (debug)
                log.info("returning list -> offset=" + offset + " size=" + subList.size() + "  queue size="
                        + current.size());
            return subList;
            // queue.remove(topic);
            // return current;
        } catch (Exception e) {
            log.severe("Error al obtener eventos " + e.getLocalizedMessage());
            return null;
        }

    }

    @Override
    public void commitEventReaded(String consumerId, String topic, String eventUuid) {
        try {
            int currentOffset = getOffset(consumerId, topic);
            int size = offsetSizeMap.get(consumerId).get(topic);
            if (debug)
                log.info("confirmando lectura de -> [" + topic + "] [" + size + "]");
            offsetMap.get(consumerId).put(topic, currentOffset + size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getOffset(String consumerId, String topic) {
        // try{
        if (!offsetMap.containsKey(consumerId)) {
            offsetMap.put(consumerId, new HashMap<>());
            offsetSizeMap.put(consumerId, new HashMap<>());
        }

        if (!offsetMap.get(consumerId).containsKey(topic)) {
            offsetMap.get(consumerId).put(topic, 0);
            offsetSizeMap.get(consumerId).put(topic, 0);
        }

        int currentOffset = offsetMap.get(consumerId).get(topic);

        if (debug)
            log.info("offset -> [" + consumerId + "]  -> [" + topic + "] --> [" + currentOffset + "]");

        return currentOffset;
        // }catc
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Topics").append("\n");
        sb.append("============================").append("\n");
        for (var item : queue.entrySet()) {
            sb.append("T: [").append(item.getKey()).append("] Mensajes: [").append(item.getValue().size()).append("] ").append("\n");
        }

        return sb.toString();
    }

}

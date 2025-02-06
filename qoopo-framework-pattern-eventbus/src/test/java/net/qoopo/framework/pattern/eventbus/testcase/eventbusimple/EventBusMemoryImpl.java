package net.qoopo.framework.pattern.eventbus.testcase.eventbusimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.Consumer;

public class EventBusMemoryImpl<KEY, VALUE> implements EventBus<KEY, VALUE> {
    // la cola de mensajes para los que aún no se han suscrito en el modelo de Kafa
    // donde se pide si hay mensajes en un topic

    private Map<String, List<EventRecord<KEY, VALUE>>> queue = new HashMap<>();

    // la lista de suscriptores en el modelo de suscriptores registrados
    private Map<String, List<Consumer<KEY, VALUE>>> suscriptors = new HashMap<>();

    @Override
    public void suscribe(String topic, Consumer<KEY, VALUE> consumer) {
        if (!suscriptors.containsKey(topic))
            suscriptors.put(topic, new ArrayList<>());
        suscriptors.get(topic).add(consumer);
    }

    @Override
    public void publish(String topic, EventRecord<KEY, VALUE> eventRecord) {

        // agrega el mensaje en la cola para el modelo sin suscripcion
        if (!queue.containsKey(topic))
            queue.put(topic, new ArrayList<>());
        queue.get(topic).add(eventRecord);

        // notifica a los suscritos
        if (suscriptors.containsKey(topic)) {
            suscriptors.get(topic).stream().parallel().forEach(c -> c.receive(eventRecord));
        }
    }

    @Override
    public List<EventRecord<KEY, VALUE>> getEvents(String topic) {
        var current=queue.get(topic);
        queue.clear();
        return current;
    }

}

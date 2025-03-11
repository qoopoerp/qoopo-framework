package net.qoopo.framework.pattern.eventbus.message;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import net.qoopo.framework.pattern.eventbus.EventBus;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;

/**
 * Servicio de mensajes para ser usado por Qoopo-framework
 */
@AllArgsConstructor
public class MessageService<T> implements EventMessagingService<T> {

    private static Logger log = Logger.getLogger("message-service");

    private EventBus<String, EventMessage<T>> bus;
    private MessageProducer<T> producer;
    private Duration sleepDuration = Duration.ofSeconds(1);
    private Duration pollDuration = Duration.ofSeconds(3);

    private Map<String, List<EventMessageHandler>> handlersMap;

    public MessageService(EventBus<String, EventMessage<T>> bus) {
        this.bus = bus;
        // configuramos con producer y luego un consumer
        producer = new MessageProducer<T>(bus);
        handlersMap = new HashMap<>();
    }

    @Override
    public void sendEvent(String destination, EventMessage<T> message) {
        EventRecord<String, EventMessage<T>> record = new EventRecord<String, EventMessage<T>>(null, destination,
                message);
        producer.send(record);
    }

    @Override
    public void receiveEvents(String destination, EventMessageHandler handler) {
        log.info("[+] Registering handler for -> " + destination);
        if (!handlersMap.containsKey(destination)) {
            handlersMap.put(destination, new ArrayList<>());
            handlersMap.get(destination).add(handler);
            startPoll(destination);
        } else
            handlersMap.get(destination).add(handler);
    }

    private void startPoll(String destination) {
        log.info("[+] Starting poller for -> " + destination);
        new Thread(new Runnable() {
            public void run() {
                MessageConsumerPoll<T> consumer = new MessageConsumerPoll<T>(bus, destination);
                while (true) {
                    // log.info("leyendo del bus ... [" + destination + "]");
                    var list = consumer.poll(pollDuration);
                    if (list != null && !list.isEmpty()) {
                        log.info("[+] Hay (" + list.size() + ") eventos en -> [" + destination + "]");
                        list.stream().parallel().forEach(event -> handlersMap.get(destination)
                                .forEach(handler -> handler.handleMessage(event.getValue())));
                        consumer.commitEventReaded();
                    }
                    try {
                        Thread.sleep(sleepDuration.toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageService").append("\n");
        sb.append("============================").append("\n");
        for (var item : handlersMap.entrySet()) {
            sb.append("T: [").append(item.getKey()).append("] Consumers: [").append(item.getValue().size()).append("] ")
                    .append("\n");
        }

        return sb.toString();
    }
}

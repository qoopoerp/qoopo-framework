package net.qoopo.framework.kafka.message;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.kafka.core.QKafkaConfig;
import net.qoopo.framework.kafka.core.QKafkaConsumer;
import net.qoopo.framework.kafka.core.QKafkaProducer;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessageHandler;
import net.qoopo.framework.pattern.eventbus.message.EventMessagingService;

@Getter
@Setter
public class QKafkaMessageService<T> implements EventMessagingService<T> {

    private static Logger log = Logger.getLogger("kafka-message-service");

    private Duration sleepDuration = Duration.ofSeconds(1);
    private Duration pollDuration = Duration.ofSeconds(3);

    private QKafkaConfig config;
    private QKafkaProducer<T> producer;
    private Map<String, List<EventMessageHandler<T>>> handlersMap;

    public QKafkaMessageService(QKafkaConfig config) {
        this.config = config;
        handlersMap = new HashMap<>();
        config();
    }

    private void config() {
        this.producer = new QKafkaProducer<>(config);
    }

    @Override
    public void sendEvent(String destination, T message) {
        EventRecord<String, T> record = new EventRecord<String, T>(null, destination,
                message);
        producer.send(record);
    }

    @Override
    public void receiveEvents(String destination, EventMessageHandler<T> handler) {
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
                QKafkaConsumer<T> consumer = new QKafkaConsumer<T>(config, destination);
                while (true) {
                    // log.info("leyendo del bus ... [" + destination + "]");
                    var list = consumer.poll(pollDuration);
                    if (list != null && !list.isEmpty()) {
                        log.info("[+] Hay (" + list.size() + ") eventos en -> [" + destination + "]");
                        log.info("->" + list.toString());

                        list.stream().parallel().forEach(event -> handlersMap.get(destination)
                                .forEach(handler -> handler.handleMessage(event.getValue())));
                        // consumer.commitEventReaded();
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
        sb.append("KafkaMessageService").append("\n");
        sb.append("============================").append("\n");
        for (var item : handlersMap.entrySet()) {
            sb.append("T: [").append(item.getKey()).append("] Consumers: [").append(item.getValue().size()).append("] ")
                    .append("\n");
        }

        return sb.toString();
    }

}

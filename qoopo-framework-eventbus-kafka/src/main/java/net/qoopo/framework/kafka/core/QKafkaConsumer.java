package net.qoopo.framework.kafka.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.consumer.ConsumerPoll;

/**
 * Consumidor Kafka
 * 
 * Usa Gson para enviar objetos serialziados como Json
 */
@Getter
@Setter
public class QKafkaConsumer<T> implements ConsumerPoll<String, T> {

    private static Logger log = Logger.getLogger("qkafka-consumer");

    private QKafkaConfig config;
    private String topic;

    private KafkaConsumer<String, T> kafkaConsumer;

    public QKafkaConsumer(QKafkaConfig config, String topic) {
        this.config = config;
        this.topic = topic;
        config();
    }

    private void config() {
        // Crear consumidor Kafka
        kafkaConsumer = new KafkaConsumer<>(config.get());
        kafkaConsumer.subscribe(Collections.singletonList(topic)); // Suscribirse al topic
    }

    @Override
    public List<EventRecord<String, T>> poll(Duration duration) {
        List<EventRecord<String, T>> returnValue = new ArrayList<>();
        ConsumerRecords<String, T> records = kafkaConsumer.poll(duration);
        for (ConsumerRecord<String, T> record : records) {
            returnValue.add(new EventRecord<String, T>(record.key(), record.value()));
        }
        return returnValue;
    }

}

package net.qoopo.framework.kafka.core;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.kafka.util.LocalDateTimeAdapter;
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

    private String host;
    private String groupId;
    private String topic;
    private Gson gson;
    private Type type;

    private KafkaConsumer<String, String> kafkaConsumer;

    public QKafkaConsumer(String host, String groupId, String topic) {
        this.host = host;
        this.groupId = groupId;
        this.topic = topic;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        config();
    }

    public QKafkaConsumer(String host, String groupId, String topic, Type type) {
        this.host = host;
        this.groupId = groupId;
        this.topic = topic;
        this.type = type;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        config();
    }

    private void config() {
        // Configuración del productor
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host); // Servidor Kafka
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Grupo de consumidores
        // Deserialización de clave
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Deserialización de valor
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Leer desde el inicio si es la primera
                                                                             // vez

        // Crear consumidor Kafka
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic)); // Suscribirse al topic
    }

    @Override
    public List<EventRecord<String, T>> poll(Duration duration) {
        List<EventRecord<String, T>> returnValue = new ArrayList<>();
        ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
        for (ConsumerRecord<String, String> record : records) {
            // System.out.printf("Mensaje recibido: key=%s, value=%s, partition=%d,
            // offset=%d%n", record.key(), record.value(), record.partition(),
            // record.offset());
            T value = null;
            if (type != null)
                value = gson.fromJson(record.value(), type);
            else
                value = gson.fromJson(record.value(), new TypeToken<T>() {
                }.getType());

            returnValue.add(new EventRecord<String, T>(record.key(), value));

        }

        return returnValue;
    }

}

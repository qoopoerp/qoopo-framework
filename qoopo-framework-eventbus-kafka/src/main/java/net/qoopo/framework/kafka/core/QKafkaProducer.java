package net.qoopo.framework.kafka.core;

import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.kafka.util.LocalDateTimeAdapter;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.producer.Producer;

/**
 * Producto Kafka
 * 
 * El producto envia los objetos convertidos en Json
 */
@Getter
@Setter
public class QKafkaProducer<T> implements Producer<String, T> {

    private String host;

    private KafkaProducer<String, String> kafkaProducer;
    private Gson gson;

    public QKafkaProducer(String host) {
        this.host = host;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        config();
    }

    private void config() {
        // Configuración del productor
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host); // Servidor Kafka
        // Serialización de clave
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Serialización de valor
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Garantiza que el mensaje se replica en todas las réplicas
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // Reintentos en caso de fallo
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);

        // Crear productor Kafka
        kafkaProducer = new KafkaProducer<>(properties);
    }

    @Override
    public void send(EventRecord<String, T> record) {
        ProducerRecord<String, String> kafkaRecord = new ProducerRecord<>(
                record.getKey(),
                null, // record.getKey(),
                gson.toJson(record.getValue()));
        // Enviar mensaje con callback
        kafkaProducer.send(kafkaRecord, (metadata, exception) -> {
            if (exception == null) {
                // System.out.printf("Mensaje enviado: topic=%s, partition=%d, offset=%d%n",
                // metadata.topic(), metadata.partition(), metadata.offset());
            } else {
                exception.printStackTrace();
            }
        });
    }

}

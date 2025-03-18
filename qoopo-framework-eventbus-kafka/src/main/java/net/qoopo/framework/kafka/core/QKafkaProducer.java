package net.qoopo.framework.kafka.core;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.Getter;
import lombok.Setter;
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

    private QKafkaConfig config;
    private KafkaProducer<String, T> kafkaProducer;

    public QKafkaProducer(QKafkaConfig config) {
        this.config = config;
        config();
    }

    private void config() {
        // Crear productor Kafka
        kafkaProducer = new KafkaProducer<>(config.get());
    }

    @Override
    public void send(EventRecord<String, T> record) {
        ProducerRecord<String, T> kafkaRecord = new ProducerRecord<>(
                record.getKey(),
                null,
                record.getValue());
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

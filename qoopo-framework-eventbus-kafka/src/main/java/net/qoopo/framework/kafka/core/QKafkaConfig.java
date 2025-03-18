package net.qoopo.framework.kafka.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QKafkaConfig implements Serializable {

    private String kafkaHost;
    @Builder.Default
    private String groupId = QKafkaConfig.class.getName();
    private Map<Object, Object> properties;

    @Builder.Default
    private String keySerializerClass = StringSerializer.class.getName();
    @Builder.Default
    // private String valueSerializerClass = JsonbSerializer.class.getName();
    private String valueSerializerClass = StringSerializer.class.getName();

    @Builder.Default
    private String keyDeserializerClass = StringDeserializer.class.getName();
    @Builder.Default
    // private String valueSerializerClass = JsonbSerializer.class.getName();
    private String valueDeserializerClass = StringDeserializer.class.getName();

    public Properties get() {

        Properties outputProperties = new Properties();

        outputProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost); // Servidor Kafka
        // Serialización de clave
        outputProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        // Serialización de valor
        outputProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
        // Garantiza que el mensaje se replica en todas las réplicas
        outputProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        // Reintentos en caso de fallo
        outputProperties.put(ProducerConfig.RETRIES_CONFIG, 3);

        // consumidor

        // outputProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost); // Servidor Kafka
        outputProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Grupo de consumidores
        // Deserialización de clave
        outputProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        // Deserialización de valor
        outputProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);

        outputProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Leer desde el inicio si es la
                                                                                   // primera

        // adiciono las configuraciones adicionales
        if (properties != null) {
            outputProperties.putAll(properties);
        }
        return outputProperties;
    }

}
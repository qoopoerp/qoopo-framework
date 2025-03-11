package net.qoopo.framework.kafka;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.reflect.TypeToken;

import net.qoopo.framework.kafka.core.QKafkaConsumer;
import net.qoopo.framework.kafka.core.QKafkaProducer;
import net.qoopo.framework.kafka.message.QKafkaMessageService;
import net.qoopo.framework.kafka.model.Color;
import net.qoopo.framework.kafka.model.Product;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.message.MessageHeaders;

public class KafkaTest {

    private static Logger log = Logger.getLogger("kafka-test");

    private static final String TOPIC = "mi_topic";
    private static final String GROUP_ID = "test_kafka_7";

    private static final String host = "laptop:9092";// 192.168.200.19:9092

    // @Test
    public void testNativeProducer() {
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Producer nativo");

            // Configuración del productor
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host); // Servidor
            // Kafka
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class.getName()); // Serialización
            // de
            // clave
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class.getName()); // Serialización
            // de
            // valor
            properties.put(ProducerConfig.ACKS_CONFIG, "all"); // Garantiza que el
            // mensaje se replica en todas las
            // réplicas
            properties.put(ProducerConfig.RETRIES_CONFIG, 3); // Reintentos en caso de
            // fallo

            // Crear productor Kafka
            KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

            // Crear un mensaje
            String key = "usuario123";
            String value = "Mensaje de prueba desde Kafka Producer en Java";

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key,
                    value);

            // Enviar mensaje con callback
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.printf("Mensaje enviado: topic=%s, partition=%d, offset=%d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    exception.printStackTrace();
                }
            });

            // Cerrar productor
            producer.flush();
            producer.close();

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Consumer nativo");

            // Configuración del productor
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host); // Servidor
            // Kafka
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID); // Grupo de
            // consumidores
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class.getName()); // Deserialización
            // de clave
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class.getName()); // Deserialización
            // de valor
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Leer
            // desde el inicio si es la primera vez

            // Crear consumidor Kafka
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList(TOPIC)); // Suscribirse al topic

            // Leer mensajes en un bucle infinito
            // while (true) {
            int mensajesRecibidos = 0;
            while (mensajesRecibidos < 2) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000)); // Espera 1 seg
                for (ConsumerRecord<String, String> record : records) {
                    mensajesRecibidos++;
                    System.out.printf("Mensaje recibido: key=%s, value=%s, partition=%d,    offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
            consumer.close();

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    // @Test
    public void testString() {

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Producer String");
            QKafkaProducer<String> producer = new QKafkaProducer<>(host);

            IntStream.range(1, 100).parallel().forEach(c -> {
                Product product = Product.builder().name("Product " + c)
                        .description("Product " + c + " - gamer")
                        .color(Color.RED)
                        .build();

                producer.send(new EventRecord<String, String>("product.saved", product.toString()));
                producer.send(new EventRecord<String, String>("product.deleted", product.toString()));
                producer.send(new EventRecord<String, String>("product.archived", product.toString()));
                producer.send(new EventRecord<String, String>("product.ready", product.toString()));
            });

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Consumer String");
            QKafkaConsumer<String> consumerSaved = new QKafkaConsumer<>(host, GROUP_ID, "product.saved");
            QKafkaConsumer<String> consumerDeleted = new QKafkaConsumer<>(host, GROUP_ID, "product.deleted");
            QKafkaConsumer<String> consumerReady = new QKafkaConsumer<>(host, GROUP_ID, "product.ready");
            int mensajesRecibidos = 0;
            while (mensajesRecibidos < 5) {
                for (EventRecord<String, String> event : consumerSaved.poll(Duration.ofSeconds(3))) {
                    mensajesRecibidos++;
                    log.info("Evento recibido Producto guardado -> " + event.getValue());
                }
            }
            for (EventRecord<String, String> event : consumerDeleted.poll(Duration.ofSeconds(3))) {
                log.info("Evento recibido Producto eliminado -> " + event.getValue());
            }
            for (EventRecord<String, String> event : consumerReady.poll(Duration.ofSeconds(3))) {
                log.info("Evento recibido Producto listo -> " + event.getValue());
            }
            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    // @Test
    public void testbinary() {
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Producer binary ");
            QKafkaProducer<Product> producer = new QKafkaProducer<>(host);

            IntStream.range(1, 100).parallel().forEach(c -> {
                Product product = Product.builder().name("Product " + c)
                        .description("Product " + c + " - gamer")
                        .color(Color.RED)
                        .build();

                producer.send(new EventRecord<String, Product>("product.bin.saved",
                        product));
                producer.send(new EventRecord<String, Product>("product.bin.deleted",
                        product));
                producer.send(new EventRecord<String, Product>("product.bin.archived",
                        product));
                producer.send(new EventRecord<String, Product>("product.bin.ready",
                        product));
            });

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Consumer object");
            QKafkaConsumer<Product> consumerSaved = new QKafkaConsumer<>(host, GROUP_ID,
                    "product.bin.saved");
            QKafkaConsumer<Product> consumerDeleted = new QKafkaConsumer<>(host,
                    GROUP_ID, "product.bin.deleted");
            QKafkaConsumer<Product> consumerReady = new QKafkaConsumer<>(host, GROUP_ID,
                    "product.bin.ready");
            int mensajesRecibidos = 0;
            while (mensajesRecibidos < 5) {
                for (EventRecord<String, Product> event : consumerSaved.poll(Duration.ofSeconds(3))) {
                    mensajesRecibidos++;
                    log.info("Evento recibido Producto guardado -> " + event.getValue());
                }
            }
            for (EventRecord<String, Product> event : consumerDeleted.poll(Duration.ofSeconds(3))) {
                log.info("Evento recibido Producto eliminado -> " + event.getValue());
            }
            for (EventRecord<String, Product> event : consumerReady.poll(Duration.ofSeconds(3))) {
                log.info("Evento recibido Producto listo -> " + event.getValue());
            }
            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    // @Test
    public void testMessages() {
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO MESSAGES");
            // Declaramos un bus de eventos

            // bus.setDebug(true);

            QKafkaMessageService<Product> service = new QKafkaMessageService<Product>(host, GROUP_ID,
                    new TypeToken<EventMessage<Product>>() {
                    }.getType());

            service.receiveEvents("message.product.saved", message -> {
                // log.info("[message] - Suscriptor 1 -- Message -> " + message.toString());
                log.info("Tipo ? " + message.getPayload().getClass());
                log.info(
                        "[message] - Suscriptor 1-> Producto guardado ->" + ((Product) message.getPayload()).getName());
            });

            service.receiveEvents("message.product.saved", message -> {
                // log.info("[message] - Suscriptor 2 - Message -> " + message.toString());
                log.info("[message] - Suscriptor 2 -> Producto guardado ->"
                        + ((Product) message.getPayload()).getName());
            });

            service.receiveEvents("message.product.deleted", message -> {
                // log.info("[message] - Message -> " + message.toString());
                log.info("[message] -> Producto eliminado ->" + ((Product) message.getPayload()).getName());
            });

            service.receiveEvents("message.product.archived", message -> {
                // log.info("[message] - Message -> " + message.toString());
                log.info("[message] -> Producto archivado ->" + ((Product) message.getPayload()).getName());
            });

            IntStream.range(1, 51).forEach(c -> {
                Product product = Product.builder().name("ProductMessage " + c)
                        .description("ProductMessage " + c + " - gamer")
                        .color(Color.RED)
                        .build();
                service.sendEvent("message.product.saved", new EventMessage<Product>(
                        MessageHeaders.builder().createAt(LocalDateTime.now())
                                .id(UUID.randomUUID().toString())
                                .source("Test")
                                .user("test-user")
                                .build(),
                        product));

                service.sendEvent("message.product.deleted", new EventMessage<Product>(
                        MessageHeaders.builder().createAt(LocalDateTime.now())
                                .id(UUID.randomUUID().toString())
                                .source("Test")
                                .user("test-user")
                                .build(),
                        product));
                service.sendEvent("message.product.archived", new EventMessage<Product>(
                        MessageHeaders.builder().createAt(LocalDateTime.now())
                                .id(UUID.randomUUID().toString())
                                .source("Test")
                                .user("test-user")
                                .build(),
                        product));

                service.sendEvent("message.product.ready", new EventMessage<Product>(
                        MessageHeaders.builder().createAt(LocalDateTime.now())
                                .id(UUID.randomUUID().toString())
                                .source("Test")
                                .user("test-user")
                                .build(),
                        product));
                try {
                    // cada 5, espera un tiempo para probar las respuestas
                    if (c % 5 == 0) {
                        log.info("\n\n\n\n\n\n\n\n");
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            });

            // damos tiempo que los lectores lean los mensajes
            Thread.sleep(5000);

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

}

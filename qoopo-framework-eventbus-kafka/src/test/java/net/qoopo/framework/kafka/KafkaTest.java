package net.qoopo.framework.kafka;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.SecureRandom;
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

import net.qoopo.framework.kafka.core.QKafkaConfig;
import net.qoopo.framework.kafka.core.QKafkaConsumer;
import net.qoopo.framework.kafka.core.QKafkaProducer;
import net.qoopo.framework.kafka.message.QKafkaMessageService;
import net.qoopo.framework.kafka.model.Color;
import net.qoopo.framework.kafka.model.Customer;
import net.qoopo.framework.kafka.model.CustomerMessage;
import net.qoopo.framework.kafka.model.Product;
import net.qoopo.framework.kafka.model.ProductMessage;
import net.qoopo.framework.kafka.serialization.gson.GsonDeserializer;
import net.qoopo.framework.kafka.serialization.gson.GsonSerializer;
import net.qoopo.framework.kafka.serialization.jsonb.JsonbDeserializer;
import net.qoopo.framework.kafka.serialization.jsonb.JsonbSerializer;
import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.message.MessageHeaders;

public class KafkaTest {

    private static Logger log = Logger.getLogger("kafka-test");

    private static final String TOPIC = "qoopo.framework.eventbus.kafka.test";
    private static final String GROUP_ID = "test_kafka_1";

    // private static final String host =
    // "localhost:29092,localhost:39092,localhost:49092";// "laptop:9092";
    private static final String host = "laptop:9092";

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
        QKafkaConfig config = QKafkaConfig.builder().kafkaHost(host).groupId(GROUP_ID).build();
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Producer String");

            QKafkaProducer<String> producer = new QKafkaProducer<>(config);

            IntStream.range(1, 100).parallel().forEach(c -> {
                Product product = Product.builder().name("Product " + c)
                        .description("Product " + c + " - gamer")
                        .color(Color.RED)
                        .build();
                producer.send(new EventRecord<String, String>(TOPIC, product.toString()));

            });

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Consumer String");
            QKafkaConsumer<String> consumerSaved = new QKafkaConsumer<>(config, TOPIC);
            int mensajesRecibidos = 0;
            while (mensajesRecibidos < 5) {
                for (EventRecord<String, String> event : consumerSaved.poll(Duration.ofSeconds(3))) {
                    mensajesRecibidos++;
                    log.info("Evento recibido Producto guardado -> " + event.getValue());
                }
            }
            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    // @Test
    public void testbinary() {

        Properties aditionalProperties = new Properties();
        aditionalProperties.setProperty(GsonDeserializer.TYPE, Product.class.getName());
        aditionalProperties.setProperty(JsonbDeserializer.TYPE, Product.class.getName());

        QKafkaConfig config = QKafkaConfig.builder().kafkaHost(host).groupId(GROUP_ID)
                .valueSerializerClass(JsonbSerializer.class.getName())
                .valueDeserializerClass(JsonbDeserializer.class.getName())
                .properties(aditionalProperties)
                .build();
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Producer binary ");
            QKafkaProducer<Product> producer = new QKafkaProducer<>(config);

            IntStream.range(1, 10).parallel().forEach(c -> {
                Product product = Product.builder().name("Product " + c)
                        .description("Product " + c + " - gamer")
                        .createAt(LocalDateTime.now())
                        .color(Color.RED)
                        .build();

                producer.send(new EventRecord<String, Product>(TOPIC, product));
            });

            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO Consumer object");
            QKafkaConsumer<Product> consumerSaved = new QKafkaConsumer<>(config, TOPIC);
            int mensajesRecibidos = 0;
            while (mensajesRecibidos < 5) {
                for (EventRecord<String, Product> event : consumerSaved.poll(Duration.ofSeconds(3))) {
                    mensajesRecibidos++;
                    log.info("Evento recibido Producto guardado -> " + event.getValue());
                }
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

            QKafkaConfig config = QKafkaConfig.builder().kafkaHost(host).groupId(GROUP_ID)
                    .valueSerializerClass(GsonSerializer.class.getName())
                    .valueDeserializerClass(GsonDeserializer.class.getName())
                    .build();

            QKafkaMessageService<EventMessage<Product>> service = new QKafkaMessageService<EventMessage<Product>>(
                    config);

            service.receiveEvents(TOPIC,
                    message -> log.info("[Evento]  Producto guardado ->" + ((Product) message.getPayload()).getName()));

            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            IntStream.range(1, 15).forEach(c -> {
                Product product = Product.builder().name("ProductMessage " + c)
                        .description("ProductMessage " + c + " - gamer")
                        .createAt(LocalDateTime.now())
                        .color(Color.RED)
                        .build();
                service.sendEvent(TOPIC,
                        new ProductMessage(
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

    // @Test
    public void testMessagesGeneric() {
        try {
            log.info("\n\n\n\n\n\n\n\n");
            log.info("PROBANDO MESSAGES Generic");

            QKafkaConfig config = QKafkaConfig.builder().kafkaHost(host).groupId(GROUP_ID)
                    .valueSerializerClass(JsonbSerializer.class.getName())
                    .valueDeserializerClass(JsonbDeserializer.class.getName())
                    .build();

            QKafkaMessageService<EventMessage<?>> service = new QKafkaMessageService<EventMessage<?>>(config);

            service.receiveEvents(TOPIC, message -> {
                if (message.getClass().isAssignableFrom(ProductMessage.class))
                    log.info(
                            "[PRODUCTO] -> Llegó Evento de producto ->"
                                    + ((Product) message.getPayload()).toString());

                if (message.getClass().isAssignableFrom(CustomerMessage.class))
                    log.info(
                            "[CUSTOMER] -  Llegó Evento de Customer ->"
                                    + ((Customer) message.getPayload()).toString());
            });

            IntStream.range(1, 20).forEach(c -> {
                Product product = Product.builder().name("ProductMessage " + c)
                        .description("ProductMessage " + c + " - gamer")
                        .createAt(LocalDateTime.now())
                        .color(Color.RED)
                        .build();
                Customer customer = Customer.builder().name("Customer " + c)
                        .age(new SecureRandom().nextDouble() * 50 + 1)
                        .birthDay(LocalDateTime.now().plusYears(-1 * (new SecureRandom().nextInt(50) + 1)))
                        .lastName(" Jaime")
                        .build();
                service.sendEvent(TOPIC,
                        new ProductMessage(
                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                        .id(UUID.randomUUID().toString())
                                        .source("Test")
                                        .user("test-user")
                                        .build(),
                                product));

                service.sendEvent(TOPIC,
                        new CustomerMessage(
                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                        .id(UUID.randomUUID().toString())
                                        .source("Customers")
                                        .user("test-user")
                                        .build(),
                                customer));

                try {
                    // cada 5, espera un tiempo para probar las respuestas
                    if (c % 5 == 0) {
                        log.info("\n\n\n\n\n\n\n\n");
                        Thread.sleep(5000);
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

    // // @Test
    // public void testStringP() {
    // String linea = "<campoAdicional nombre=\"SALDO
    // ANTERIOR\">0.00</campoAdicional>";
    // log.info("Atributo=" + linea.substring(linea.indexOf("\"") + 1,
    // linea.indexOf("\"", linea.indexOf("\"") + 1)));
    // log.info("Valor=" + linea.substring(linea.indexOf(">") + 1,
    // linea.indexOf("</")));
    // assertTrue(true);

    // linea = "<detAdicional nombre=\"imei/imsi\" valor=\"4521858552218585414\"/>";
    // int pUbicacion = linea.indexOf("nombre=\"") + 8;
    // log.info("Atributo=" + linea.substring(pUbicacion, linea.indexOf("\"",
    // pUbicacion + 1)));
    // pUbicacion = linea.indexOf("valor=\"") + 7;
    // log.info("Valor=" + linea.substring(pUbicacion, linea.indexOf("\"",
    // pUbicacion + 1)));
    // }
}

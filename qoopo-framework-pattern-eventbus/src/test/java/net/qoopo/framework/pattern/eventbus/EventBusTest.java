package net.qoopo.framework.pattern.eventbus;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.message.MessageHeaders;
import net.qoopo.framework.pattern.eventbus.testcase.eventbusimple.EventBusMemoryImpl;
import net.qoopo.framework.pattern.eventbus.testcase.eventbusimple.ProductConsumer;
import net.qoopo.framework.pattern.eventbus.testcase.eventbusimple.ProductConsumerSuscription;
import net.qoopo.framework.pattern.eventbus.testcase.eventbusimple.ProductProducer;
import net.qoopo.framework.pattern.eventbus.testcase.message.ProductMessageService;
import net.qoopo.framework.pattern.eventbus.testcase.model.Color;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

public class EventBusTest {

        private static Logger log = Logger.getLogger("eventbus-test");

        @Test
        public void testEventbus() {
                try {
                        // Declaramos un bus de eventos
                        EventBusMemoryImpl<String, Product> bus = new EventBusMemoryImpl<>();

                        ProductConsumer consumerSaved = new ProductConsumer("Guardado");
                        ProductConsumer consumerDeleted = new ProductConsumer("Eliminado");
                        ProductConsumer consumerArchived = new ProductConsumer("Archivado");

                        ProductProducer producer = new ProductProducer(bus);

                        bus.suscribe("product.saved", consumerSaved);
                        bus.suscribe("product.deleted", consumerDeleted);
                        bus.suscribe("product.archived", consumerArchived);

                        IntStream.range(1, 100).parallel().forEach(c -> {
                                Product product = Product.builder().name("Product " + c)
                                                .description("Product " + c + " - gamer")
                                                .color(Color.RED)
                                                .build();
                                producer.send(new EventRecord<String, Product>("product.saved", product));
                                producer.send(new EventRecord<String, Product>("product.deleted", product));
                                producer.send(new EventRecord<String, Product>("product.archived", product));
                                producer.send(new EventRecord<String, Product>("product.ready", product));
                        });

                } catch (Exception ex) {
                        ex.printStackTrace();
                        assertTrue(false);
                }
        }

        @Test
        public void testSuscriptionConsumer() {
                try {
                        // Declaramos un bus de eventos
                        EventBusMemoryImpl<String, Product> bus = new EventBusMemoryImpl<>();

                        ProductConsumerSuscription consumerSaved = new ProductConsumerSuscription("Guardado", bus);
                        ProductConsumerSuscription consumerDeleted = new ProductConsumerSuscription("Eliminado", bus);
                        ProductConsumerSuscription consumerArchived = new ProductConsumerSuscription("Archivado", bus);

                        ProductProducer producer = new ProductProducer(bus);

                        consumerSaved.suscribe("product.saved");
                        consumerDeleted.suscribe("product.deleted");
                        consumerArchived.suscribe("product.archived");

                        IntStream.range(1, 100).parallel().forEach(c -> {
                                Product product = Product.builder().name("ProductSuscription " + c)
                                                .description("ProductSuscription " + c + " - gamer")
                                                .color(Color.RED)
                                                .build();
                                producer.send(new EventRecord<String, Product>("product.saved", product));
                                producer.send(new EventRecord<String, Product>("product.deleted", product));
                                producer.send(new EventRecord<String, Product>("product.archived", product));
                                producer.send(new EventRecord<String, Product>("product.ready", product));
                        });

                } catch (Exception ex) {
                        ex.printStackTrace();
                        assertTrue(false);
                }
        }

        @Test
        public void testMessages() {
                try {
                        log.info("PROBANDO MESSAGES");
                        // Declaramos un bus de eventos
                        EventBusMemoryImpl<String, EventMessage<Product>> bus = new EventBusMemoryImpl<>();

                        ProductMessageService service = new ProductMessageService(bus);

                        service.receiveEvents("product.saved", message -> {
                                log.info("[message] - Headers -> " + message.toString());
                                log.info("[message] -> Producto guardado ->"
                                                + ((Product) message.getPayload()).getName());
                        });

                        service.receiveEvents("product.deleted", message -> {
                                log.info("[message] - Headers -> " + message.toString());
                                log.info("[message] -> Producto eliminado ->"
                                                + ((Product) message.getPayload()).getName());
                        });

                        service.receiveEvents("product.archived", message -> {
                                log.info("[message] - Headers -> " + message.toString());
                                log.info("[message] -> Producto archivado ->"
                                                + ((Product) message.getPayload()).getName());
                        });

                        IntStream.range(1, 25).forEach(c -> {
                                Product product = Product.builder().name("ProductMEssage " + c)
                                                .description("ProductSuscription  " + c + " - gamer")
                                                .color(Color.RED)
                                                .build();
                                service.sendEvent("product.saved", new EventMessage<Product>(
                                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                                                .id(UUID.randomUUID().toString())
                                                                .source("Test")
                                                                .user("test-user")
                                                                .build(),
                                                product));

                                service.sendEvent("product.deleted", new EventMessage<Product>(
                                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                                                .id(UUID.randomUUID().toString())
                                                                .source("Test")
                                                                .user("test-user")
                                                                .build(),
                                                product));
                                service.sendEvent("product.archived", new EventMessage<Product>(
                                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                                                .id(UUID.randomUUID().toString())
                                                                .source("Test")
                                                                .user("test-user")
                                                                .build(),
                                                product));

                                service.sendEvent("product.ready", new EventMessage<Product>(
                                                MessageHeaders.builder().createAt(LocalDateTime.now())
                                                                .id(UUID.randomUUID().toString())
                                                                .source("Test")
                                                                .user("test-user")
                                                                .build(),
                                                product));
                                try {
                                        // cada 5 espera 3 segundos
                                        if (c % 5 == 0)
                                                Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }

                        });

                        // damos tiempo que los lectoes lean los mensajes
                        Thread.sleep(1500);

                        assertTrue(true);
                } catch (Exception ex) {
                        ex.printStackTrace();
                        assertTrue(false);
                }
        }

}

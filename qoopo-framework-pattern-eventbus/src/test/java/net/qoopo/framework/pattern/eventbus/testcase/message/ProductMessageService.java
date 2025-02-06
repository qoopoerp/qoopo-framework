package net.qoopo.framework.pattern.eventbus.testcase.message;

import java.time.Duration;

import net.qoopo.framework.pattern.eventbus.common.EventRecord;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.message.EventMessageHandler;
import net.qoopo.framework.pattern.eventbus.message.EventMessagingService;
import net.qoopo.framework.pattern.eventbus.testcase.eventbusimple.EventBusMemoryImpl;
import net.qoopo.framework.pattern.eventbus.testcase.model.Product;

/**
 * 
 */
public class ProductMessageService implements EventMessagingService<Product> {

    private EventBusMemoryImpl<String, EventMessage<Product>> bus;

    private ProductMessageProducer producer;

    public ProductMessageService(EventBusMemoryImpl<String, EventMessage<Product>> bus) {
        this.bus = bus;
        // configuramos con producer y luego un consumer

        producer = new ProductMessageProducer(bus);

    }

    @Override
    public void sendEvent(String destination, EventMessage<Product> message) {

        EventRecord<String, EventMessage<Product>> record = new EventRecord<String, EventMessage<Product>>(destination,
                message);
        producer.send(record);
    }

    @Override
    public void receiveEvents(String destination, EventMessageHandler handler) {

        new Thread(new Runnable() {
            public void run() {
                ProductMessageConsumerPoll consumer = new ProductMessageConsumerPoll(bus, destination);
                while (true) {
                    var list = consumer.poll(Duration.ofSeconds(3));
                    if (list != null) {
                        list.stream().parallel().forEach(event -> handler.handleMessage(event.getValue()));
                    }
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}

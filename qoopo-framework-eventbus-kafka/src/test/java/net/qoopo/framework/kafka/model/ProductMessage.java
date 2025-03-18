package net.qoopo.framework.kafka.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.qoopo.framework.pattern.eventbus.message.EventMessage;
import net.qoopo.framework.pattern.eventbus.message.MessageHeaders;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductMessage extends EventMessage<Product> {

    public ProductMessage(MessageHeaders headers, Product payload) {
        super(headers, payload);
    }
}
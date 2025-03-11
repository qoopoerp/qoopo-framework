package net.qoopo.framework.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class Product {
    private String name;
    private String description;
    private Double price;
    private Color color;
    private Size size;
}

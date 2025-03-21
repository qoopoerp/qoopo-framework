package net.qoopo.framework.kafka.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
    private String name;
    private LocalDateTime createAt;
    private String description;
    private Double price;
    private Color color;
    private Size size;
}

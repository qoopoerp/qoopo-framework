package net.qoopo.framework.kafka.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class Customer {
    private String name;
    private String lastName;
    private LocalDateTime birthDay;
    private Double age;
}

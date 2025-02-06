package net.qoopo.framework.pattern.eventbus.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class MessageHeaders implements Serializable {
    private String id;
    private String user;
    private LocalDateTime createAt;
    private String source;
}

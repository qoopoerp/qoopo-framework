package net.qoopo.framework.web.components.daterange;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RangeItem implements Serializable {
    protected Long id;
    protected String name;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
}

package net.qoopo.framework.util;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;

@Getter
@Setter
@Builder
public class ActionWrapper implements Serializable {

    private String name;
    private String description;
    private Accion action;
    private String url;
    private String accessKey;

}

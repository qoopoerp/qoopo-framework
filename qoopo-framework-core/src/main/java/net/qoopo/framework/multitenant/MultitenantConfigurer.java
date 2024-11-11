package net.qoopo.framework.multitenant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultitenantConfigurer {

    private boolean enabled = false;

    public MultitenantConfigurer enable() {
        this.enabled = true;
        return this;
    }

    public MultitenantConfigurer disable() {
        this.enabled = false;
        return this;
    }

}

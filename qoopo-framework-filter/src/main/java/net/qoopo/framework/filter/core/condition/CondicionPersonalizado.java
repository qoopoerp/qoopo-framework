package net.qoopo.framework.filter.core.condition;

public abstract class CondicionPersonalizado extends Condition {

    public abstract void run();

    @Override
    public CondicionPersonalizado clonar() {
        return this;
    }

}

package net.qoopo.framework.jpa.filter.condicion;

public abstract class CondicionPersonalizado extends Condicion {

    public abstract void run();

    @Override
    public CondicionPersonalizado clonar() {
        return this;
    }

}
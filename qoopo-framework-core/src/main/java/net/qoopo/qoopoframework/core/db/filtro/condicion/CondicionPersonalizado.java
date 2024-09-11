package net.qoopo.qoopoframework.core.db.filtro.condicion;

public abstract class CondicionPersonalizado extends Condicion {

    public abstract void run();

    @Override
    public CondicionPersonalizado clonar() {
        return this;
    }

}

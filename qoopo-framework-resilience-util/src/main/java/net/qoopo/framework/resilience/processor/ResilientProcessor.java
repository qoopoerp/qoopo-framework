package net.qoopo.framework.resilience.processor;

/**
 * Realiza un procesamiento de un item de la cola
 */
public interface ResilientProcessor<TYPE_RESPONSE, TYPE_QUEUE> {

    public TYPE_RESPONSE process(TYPE_QUEUE item );
}

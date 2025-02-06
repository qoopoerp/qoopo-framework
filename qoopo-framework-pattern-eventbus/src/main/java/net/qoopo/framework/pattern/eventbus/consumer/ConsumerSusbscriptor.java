package net.qoopo.framework.pattern.eventbus.consumer;

public interface ConsumerSusbscriptor<KEY, VALUE> extends Consumer<KEY, VALUE> {
    public void suscribe(String topic);
}

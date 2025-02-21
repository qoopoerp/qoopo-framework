package net.qoopo.framework.resilience.queue;

public interface ResilientQueueRepository<TYPE> {

    public TYPE saveItem(TYPE item);

    public void save(Iterable<TYPE> items);

    public Iterable<TYPE> load();
}

package net.qoopo.framework.resilience.queue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class DefaultResilientQueue<TYPE> implements ResilientQueue<TYPE> {

    private static final Logger LOG = Logger.getLogger(DefaultResilientQueue.class.getName());

    private Queue<TYPE> queue = new LinkedList<TYPE>();
    private ResilientQueueRepository<TYPE> repository;

    public DefaultResilientQueue(ResilientQueueRepository<TYPE> repository) {
        this.repository = repository;
    }

    public DefaultResilientQueue(Queue<TYPE> queue, ResilientQueueRepository<TYPE> repository) {
        this.queue = queue;
        this.repository = repository;
    }

    public void add(TYPE item) {
        new Thread(() -> {
            if (repository != null)
                queue.add(repository.saveItem(item));
            else
                queue.add(item);
        }).start();
        LOG.info("Item added to queue");
    }

    public TYPE poll() {
        if (!queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    @Override
    public void load() {
        queue.clear();
        if (repository != null)
            queue.addAll((Collection<? extends TYPE>) repository.load());
        LOG.info("Queue loaded from repository");
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public int getSize() {
        return queue.size();
    }

    @Override
    public Iterable<TYPE> getItems() {
        return queue;
    }

}

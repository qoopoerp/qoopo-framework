package net.qoopo.framework.resilience.bulkhead;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.decorators.Decorators;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.resilience.processor.ResilientProcessor;
import net.qoopo.framework.resilience.queue.ResilientQueue;

/**
 * Clase que administra un ThreadPoolBulkhead
 * 
 * @param <TYPE_RESPONSE>
 * @param <TYPE_QUEUE>
 */
@Getter
@Setter
public class BulkheadManager<TYPE_RESPONSE, TYPE_QUEUE> {

    private static final Logger LOG = Logger.getLogger(BulkheadManager.class.getName());

    public static final int DEFAULT_CORE_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() > 1
            ? Runtime.getRuntime().availableProcessors()
                    - 1
            : 1;
    public static final int DEFAULT_MAX_THREAD_POOL_SIZE = Runtime.getRuntime()
            .availableProcessors();

    private ThreadPoolBulkhead bulkhead;
    private CompletionStage<TYPE_RESPONSE> execution;
    private ResilientQueue<TYPE_QUEUE> queue;
    private ResilientProcessor<TYPE_RESPONSE, TYPE_QUEUE> processor;

    private boolean running = false;

    private String name;
    private int maxThreadPoolSize = DEFAULT_MAX_THREAD_POOL_SIZE;
    private int coreThreadPoolSize = DEFAULT_CORE_THREAD_POOL_SIZE;

    private int queueCapacity = 100000;

    private Long timeWaitInMillis = 500L;

    public BulkheadManager(String name, ResilientQueue<TYPE_QUEUE> queue,
            ResilientProcessor<TYPE_RESPONSE, TYPE_QUEUE> processor) {
        this.name = name;
        this.queue = queue;
        this.processor = processor;
        configure();
    }

    public BulkheadManager(String name, ResilientQueue<TYPE_QUEUE> queue,
            ResilientProcessor<TYPE_RESPONSE, TYPE_QUEUE> processor, int maxThreadPoolSize, int coreThreadPoolSize,
            int queueCapacity) {
        this.name = name;
        this.queue = queue;
        this.processor = processor;
        this.maxThreadPoolSize = maxThreadPoolSize;
        this.coreThreadPoolSize = coreThreadPoolSize;
        this.queueCapacity = queueCapacity;
        configure();
    }

    private void configure() {

        // Create a CircuitBreaker with default configuration
        // CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("edocuments");

        // Create a Retry with default configuration
        // 3 retry attempts and a fixed time interval between retries of 500ms
        // Retry retry = Retry.ofDefaults("backendService");

        // Create a custom ThreadPoolBulkhead configuration
        ThreadPoolBulkheadConfig bhConfig = ThreadPoolBulkheadConfig
                // .ofDefaults();
                .custom()
                .maxThreadPoolSize(maxThreadPoolSize)
                .coreThreadPoolSize(coreThreadPoolSize)
                .queueCapacity(queueCapacity)
                .build();

        // Supplier<WSRespuesta> supplier = () -> processor.procesar(queue.getNext());

        bulkhead = ThreadPoolBulkhead.of(name, bhConfig);

        // bulkhead.getEventPublisher()
        // .onCallPermitted(e -> log.info("Event Permited ->" + e.getEventType()))
        // .onCallRejected(e -> log.info("Event Rejected -> " + e.getEventType()))
        // .onCallFinished(e -> log.info("Event finished -> " + e.getEventType()));
    }

    /**
     * Inicia el BulkheadManager
     */
    public void start() {
        running = true;
        queue.load();
        mainLoop();
    }

    /**
     * Detiene el BulkheadManager
     */
    public void stop() {
        running = false;
    }

    /**
     * Reinicia el BulkheadManager
     */
    public void restart() {
        stop();
        start();
    }

    /**
     * Loop principal
     */
    private void mainLoop() {
        while (running) {
            run();
            try {
                Thread.sleep(timeWaitInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOG.info("BulkheadManager stopped " + name);
    }

    /**
     * Ejecuta el procesador
     */
    private void run() {
        try {
            execution = Decorators.ofSupplier(() -> processor.process(queue.poll()))
                    .withThreadPoolBulkhead(bulkhead)
                    .withFallback(List.of(BulkheadFullException.class), this::runFallBack)
                    .get();

            // execution.toCompletableFuture().get()
            // execution = bulkhead.executeSupplier(() -> processor.procesar());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TYPE_RESPONSE runFallBack(Throwable e) {
        LOG.log(Level.SEVERE, "Error al procesar ", e);
        e.printStackTrace();
        return null;
    }
}

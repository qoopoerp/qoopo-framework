package net.qoopo.framework.tasks;

import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Ejecuta el recolector de basura
 *
 * @author alberto
 */
@Task(name = "Task GC", description = "Tarea que se encarga de realizar la recolección de basura", interval = 1000L
        * 60L * 5L, runStart = true)
public class TareaGC extends TimerTask {

    public static final Logger log = Logger.getLogger("TareaGC");

    public TareaGC() {
        //
    }

    @Override
    public void run() {
        // log.fine("Qoopo. Garbage Collector");
        try {
            System.gc();
            // Runtime garbage = Runtime.getRuntime();
            // log.info("[+] Liberando memoria");
            // log.info("[+] Memoria libre antes: " + (garbage.freeMemory() / 1024) / 1024 +
            // " Mb de " + (garbage.totalMemory() / 1024) / 1024 + " Mb");
            // garbage.gc();
            // log.info("[+] Memoria libre despues: " + (garbage.freeMemory() / 1024) / 1024
            // + " Mb de " + (garbage.totalMemory() / 1024) / 1024 + " Mb");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

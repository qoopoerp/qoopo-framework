package net.qoopo.framework.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Define un servicio de Qoopo que debe ser ejecutado al cargar el sistema
 *
 * @author alberto
 */
public abstract class QoopoService implements Serializable, Runnable {

    public static final Logger log = Logger.getLogger("Qoopo-services");
    private static final List<QoopoService> INSTANCES = new ArrayList<>();

    public static void load() {
        try {
            INSTANCES.clear();
            ServiceLoader<QoopoService> cargados = ServiceLoader.load(QoopoService.class);
            cargados.forEach(task -> {
                log.info("[+] Servicio cargado: [".concat(task.getName()).concat("] ")
                        .concat(task.getClass().getName()));
                INSTANCES.add(task);
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Inicia los servicios registrados
     */
    public static void start() {
        try {
            log.info("Ejecutando Servicios.");
            for (QoopoService service : INSTANCES) {
                log.log(Level.INFO, "QoopoService--> Ejecutando servicio [{0}]", service.getName());
                new Thread(service).start();
            }
        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
        }
    }

    protected String name;

    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

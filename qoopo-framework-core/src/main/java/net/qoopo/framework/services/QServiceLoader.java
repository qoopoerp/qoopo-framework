package net.qoopo.framework.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.framework.reflection.QoopoReflection;

/**
 * Define un servicio de Qoopo que debe ser ejecutado al cargar el sistema
 * 
 * Ejemplo:
 * - EmailService
 *
 * @author alberto
 */
public final class QServiceLoader {

    public static final Logger log = Logger.getLogger("QServiceLoader");
    private static final List<Runnable> INSTANCES = new ArrayList<>();

    private static final List<Object> INSTANCES_SERVICES = new ArrayList<>();

    public static void load() {
        loadBackgroundServices();
        loadServices();
    }

    private static void loadBackgroundServices() {
        try {
            INSTANCES.clear();
            log.info("[+] Cargando servicios background");
            List<Object> services = QoopoReflection.getBeanAnnotaded(BackgroundService.class);
            for (Object instancia : services) {
                BackgroundService anotacion = instancia.getClass().getAnnotation(BackgroundService.class);
                if (instancia instanceof Runnable) {
                    INSTANCES.add((Runnable) instancia);
                    log.info("[+] Servicio cargado: [".concat(anotacion.name()));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            start();
        }
    }

    private static void loadServices() {
        try {
            INSTANCES_SERVICES.clear();
            log.info("[+] Cargando servicios");
            // List<Object> cargados = QoopoReflection.getBeanImplemented(Service.class);
            List<Object> cargados = QoopoReflection.getBeanAnnotaded(Service.class);
            cargados.forEach(instancia -> {
                // Service controller = (Service) instancia;
                // Service anotacion = instancia.getClass().getAnnotation(Service.class);
                log.info("[+] Service cargado: [".concat(instancia.getClass().getName()).concat("] "));
                INSTANCES_SERVICES.add(instancia);
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Inicia los servicios registrados
     */
    private static void start() {
        try {
            log.info("Ejecutando Servicios.");
            for (Runnable service : INSTANCES) {
                log.log(Level.INFO, "QoopoService--> Ejecutando servicio [{0}]", service.getClass().getName());
                new Thread(service).start();
            }
        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
        }
    }

}

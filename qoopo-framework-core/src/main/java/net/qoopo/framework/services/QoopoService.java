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
public final class QoopoService {

    public static final Logger log = Logger.getLogger("Qoopo-services");
    private static final List<Runnable> INSTANCES = new ArrayList<>();

    public static void load() {
        try {
            INSTANCES.clear();
            log.info("[+] Cargando servicios ");
            // ServiceLoader<QoopoService> cargados =
            // ServiceLoader.load(QoopoService.class);
            // cargados.forEach(task -> {
            // log.info("[+] Servicio cargado: [".concat(task.getName()).concat("] ")
            // .concat(task.getClass().getName()));
            // INSTANCES.add(task);
            // });

            List<Object> services = QoopoReflection.getBeanAnnotaded(Service.class);
            for (Object instancia : services) {
                Service anotacion = instancia.getClass().getAnnotation(Service.class);
                if (instancia instanceof Runnable) {
                    INSTANCES.add((Runnable) instancia);
                    log.info("[+] Servicio cargado: [".concat(anotacion.name()));
                }
            }
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
            for (Runnable service : INSTANCES) {
                log.log(Level.INFO, "QoopoService--> Ejecutando servicio [{0}]", service.getClass().getName());
                new Thread(service).start();
            }
        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
        }
    }

}

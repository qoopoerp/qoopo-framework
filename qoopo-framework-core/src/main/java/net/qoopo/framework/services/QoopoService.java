package net.qoopo.framework.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

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

            Reflections reflections = new Reflections("net.qoopo", Scanners.TypesAnnotated);
            Set<Class<?>> clasesAnotadas = reflections.getTypesAnnotatedWith(Service.class);
            // Imprimimos las clases encontradas
            for (Class<?> clase : clasesAnotadas) {
                Service anotacion = clase.getAnnotation(Service.class);
                Object instancia = crearInstancia(clase);
                if (instancia instanceof Runnable)
                    INSTANCES.add((Runnable) instancia);
                log.info("[+] Servicio cargado: [".concat(anotacion.name()));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    // Método genérico para crear una instancia de cualquier clase
    public static Object crearInstancia(Class<?> clase)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // Obtener el constructor predeterminado (sin parámetros)
        Constructor<?> constructor = clase.getDeclaredConstructor();

        // Asegurarnos de que el constructor sea accesible
        constructor.setAccessible(true);

        // Crear y devolver una nueva instancia
        return constructor.newInstance();
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

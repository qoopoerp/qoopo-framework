package net.qoopo.framework.tasks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.date.FechasUtil;
import net.qoopo.framework.util.QLogger;

/**
 * Define una Tarea de Qoopo que debe ser ejecutado cada cierto tiempo
 * configurado en la variable interval
 *
 * @author alberto
 */
// net.qoopo.qoopoframework.tasks.QoopoTask
@Getter
@Setter
public final class QoopoTask {

    public static final Logger log = Logger.getLogger("Qoopo-tasks");
    private static final List<TimerTask> INSTANCES = new ArrayList<>();
    private static final Timer QOOPO_TIMER = new Timer();

    public static void load() {
        try {
            INSTANCES.clear();
            log.info("[+] Cargando tareas programadas");
            // ServiceLoader<QoopoTask> cargados = ServiceLoader.load(QoopoTask.class);
            // cargados.forEach(task -> {
            // log.info("[+] Tarea cargada: [".concat(task.getName()).concat("]
            // ").concat(task.getClass().getName()));
            // INSTANCES.add(task);
            // });

            // tarea de las reglas de inventario que se ejecuta a la media noche
            Calendar c = Calendar.getInstance();// toma la fecha de hoy y la programa a la media noche
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Reflections reflections = new Reflections("net.qoopo", Scanners.TypesAnnotated);
            Set<Class<?>> clasesAnotadas = reflections.getTypesAnnotatedWith(Task.class);
            // Imprimimos las clases encontradas
            for (Class<?> clase : clasesAnotadas) {
                Task anotacion = clase.getAnnotation(Task.class);
                Object task = crearInstancia(clase);
                if (task instanceof TimerTask) {
                    INSTANCES.add((TimerTask) task);

                    if (anotacion.timeDelayed() == -1) {
                        log.log(Level.INFO,
                                "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                                new Object[] { anotacion.name(), sdf.format(c.getTime()),
                                        QLogger.getTimeFormater(anotacion.interval()) });
                        QOOPO_TIMER.schedule((TimerTask) task, c.getTime(), anotacion.interval());
                    } else {
                        LocalDateTime startDateTime = LocalDateTime.now().plusSeconds(anotacion.timeDelayed() / 1000);
                        log.log(Level.INFO,
                                "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                                new Object[] { anotacion.name(),
                                        sdf.format(FechasUtil.convertLocalDate(startDateTime)),
                                        QLogger.getTimeFormater(anotacion.interval()) });
                        QOOPO_TIMER.schedule((TimerTask) task, FechasUtil.convertLocalDate(startDateTime),
                                anotacion.interval());
                    }
                    if (anotacion.runStart()) {
                        ((TimerTask) task).run();
                    }

                    log.info("[+] Tarea agregada: [".concat(anotacion.name()));
                } else {
                    log.severe("[x] Descartando tarea que no hereda de TimerTask [" +anotacion.name()+"]");
                }
            }
        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
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

    public static List<TimerTask> get() {
        return INSTANCES;
    }

    // public static void start() {

    // // try {
    // // log.info("Qoopo. Configurando recolector de basura.");
    // // log.info("Qoopo. La tarea de Recolección se ejecutará cada 5 minutos");
    // // QOOPO_TIMER.schedule(new TareaGC(), 10000L, 1000L * 60L * 5L);// En 10
    // segundos y luego cada 5 minutos
    // // } catch (Exception e) {
    // // log.severe("Error configurando recolector de basura");
    // // }
    // }

    // protected LocalDateTime startDate = null;

    // /**
    // * Nombre del servicio
    // */
    // protected String name;
    // /**
    // * Intervalo en milisegundos entre cada ejecucion
    // */
    // protected Long interval;

    // /**
    // * Indica si la tarea se debe ejecutar al arrancar el sistema
    // */
    // protected boolean runStart = false;

    // public String getName() {
    // return name;
    // }

    // public void setName(String name) {
    // this.name = name;
    // }

    // public Long getInterval() {
    // return interval;
    // }

    // public void setInterval(Long interval) {
    // this.interval = interval;
    // }

}

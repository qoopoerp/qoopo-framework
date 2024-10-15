package net.qoopo.qoopoframework.tasks;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.util.QLogger;
import net.qoopo.util.date.FechasUtil;

/**
 * Define una Tarea de Qoopo que debe ser ejecutado cada cierto tiempo
 * configurado en la variable interval
 *
 * @author alberto
 */
//net.qoopo.qoopoframework.tasks.QoopoTask
@Getter
@Setter
public abstract class QoopoTask extends TimerTask {

    public static final Logger log = Logger.getLogger("Qoopo-tasks");
    private static final List<QoopoTask> INSTANCES = new ArrayList<>();
    private static final Timer QOOPO_TIMER = new Timer();

    protected LocalDateTime startDate = null;

    public static void load() {
        try {
            INSTANCES.clear();
            ServiceLoader<QoopoTask> cargados = ServiceLoader.load(QoopoTask.class);
            cargados.forEach(task -> {
                log.info("[+] Tarea cargada: [".concat(task.getName()).concat("] ").concat(task.getClass().getName()));
                INSTANCES.add(task);
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static List<QoopoTask> get() {
        return INSTANCES;
    }

    public static void start() {
        try {
            log.info(" Configurando tareas programadas.");
            // tarea de las reglas de inventario que se ejecuta a la media noche
            Calendar c = Calendar.getInstance();// toma la fecha de hoy y la programa a la media noche
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (QoopoTask task : QoopoTask.get()) {

                if (task.getStartDate() == null) {
                    log.log(Level.INFO,
                            "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                            new Object[] { task.getName(), sdf.format(c.getTime()),
                                    QLogger.getTimeFormater(task.getInterval()) });
                    QOOPO_TIMER.schedule(task, c.getTime(), task.getInterval());
                } else {
                    log.log(Level.INFO,
                            "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                            new Object[] { task.getName(), sdf.format(FechasUtil.convertLocalDate(task.getStartDate())),
                                    QLogger.getTimeFormater(task.getInterval()) });
                    QOOPO_TIMER.schedule(task, FechasUtil.convertLocalDate(task.getStartDate()), task.getInterval());
                }
                if (task.isRunStart()) {
                    task.run();
                }
            }
        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
        }

        try {
            log.info("Qoopo. Configurando recolector de basura.");
            log.info("Qoopo. La tarea de Recolección se ejecutará cada 5 minutos");
            QOOPO_TIMER.schedule(new TareaGC(), 10000L, 1000L * 60L * 5L);// En 10 segundos y luego cada 5 minutos
        } catch (Exception e) {
            log.severe("Error configurando recolector de basura");
        }
    }

    /**
     * Nombre del servicio
     */
    protected String name;
    /**
     * Intervalo en milisegundos entre cada ejecucion
     */
    protected Long interval;

    /**
     * Indica si la tarea se debe ejecutar al arrancar el sistema
     */
    protected boolean runStart = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

}

package net.qoopo.framework.tasks;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.date.FechasUtil;
import net.qoopo.framework.reflection.QoopoReflection;
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
public final class QTaskLoader {

    public static final Logger log = Logger.getLogger("Qoopo-tasks");
    private static final List<TimerTask> INSTANCES = new ArrayList<>();
    private static final Timer QOOPO_TIMER = new Timer();

    public static void load() {
        try {
            INSTANCES.clear();
            log.info("[+] Cargando tareas programadas");

            // tarea de las reglas de inventario que se ejecuta a la media noche
            Calendar c = Calendar.getInstance();// toma la fecha de hoy y la programa a la media noche
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            List<Object> implementations = QoopoReflection.getBeanAnnotaded(Task.class);
            for (Object instancia : implementations) {
                Task anotacion = instancia.getClass().getAnnotation(Task.class);
                if (instancia instanceof TimerTask) {
                    INSTANCES.add((TimerTask) instancia);

                    if (anotacion.timeDelayed() == -1) {
                        log.log(Level.INFO,
                                "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                                new Object[] { anotacion.name(), sdf.format(c.getTime()),
                                        QLogger.getTimeFormater(anotacion.interval()) });
                        QOOPO_TIMER.schedule((TimerTask) instancia, c.getTime(), anotacion.interval());
                    } else {
                        LocalDateTime startDateTime = LocalDateTime.now().plusSeconds(anotacion.timeDelayed() / 1000);
                        log.log(Level.INFO,
                                "QOOPO_TIMER--> Configurando la tarea {0} para ejecutarse a partir de {1} cada {2} ",
                                new Object[] { anotacion.name(),
                                        sdf.format(FechasUtil.convertLocalDate(startDateTime)),
                                        QLogger.getTimeFormater(anotacion.interval()) });
                        QOOPO_TIMER.schedule((TimerTask) instancia, FechasUtil.convertLocalDate(startDateTime),
                                anotacion.interval());
                    }
                    if (anotacion.runStart()) {
                        ((TimerTask) instancia).run();
                    }

                    log.info("[+] Tarea agregada: [".concat(anotacion.name()));
                }
            }

        } catch (Exception e) {
            log.severe("Error configurando tareas programadas");
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static List<TimerTask> get() {
        return INSTANCES;
    }

}

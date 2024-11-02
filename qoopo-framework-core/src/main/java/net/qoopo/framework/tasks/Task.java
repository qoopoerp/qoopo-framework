package net.qoopo.framework.tasks;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface Task {

    /**
     * Nombre del servicio
     */
    String name();

    String description() default "";

    /**
     * * Intervalo en milisegundos entre cada ejecucion
     * 
     * @return
     */
    long interval();

    /**
     * Indica si la tarea se debe ejecutar al arrancar el sistema
     * 
     * @return
     */
    boolean runStart() default false;

    /**
     * Indica si la tarea está habilitada
     * 
     * @return
     */
    boolean enabled() default true;

    /**
     * Indica el tiempo que se demora en iniciar la tarea si runStart es false
     * Si es -1  se programa para iniciar a la media noche
     * @return
     */
    long timeDelayed() default -1;
}

package net.qoopo.framework;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.controller.QControllerManager;
import net.qoopo.framework.services.QoopoService;
import net.qoopo.framework.tasks.QoopoTask;

/**
 * QoopoFramework es un conjunto de rutinas y utilitarios que serán utilizados
 * en las aplicaciones Qoopo
 * 
 * Features:
 * 
 * - Acceso al controlador JPA para el acceso a la base de datos GenericBusiness
 * -
 * -
 * 
 * @param datasourceName
 * @return
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class QoopoFramework {

    private static QoopoFramework INSTANCE = null;

    private String dataSourceName;

    public static final String version = "1.0.0-beta";

    private QoopoFramework() {
        //
    }

    public static void config(QoopoFramework qf) {
        INSTANCE = qf;
    }

    public static QoopoFramework get() {
        if (INSTANCE == null) {
            INSTANCE = new QoopoFramework();
        }
        return INSTANCE;
    }

    /**
     * Carga todas las implementaciones que necesita el framework como servicios,
     * tareas
     */
    public void load() {
        QoopoTask.load();
        // QoopoTask.start();
        QoopoService.load();
        QoopoService.start();
        QControllerManager.load();
    }
}

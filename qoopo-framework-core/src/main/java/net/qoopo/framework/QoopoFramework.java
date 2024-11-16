package net.qoopo.framework;


import java.util.function.Function;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.controller.QControllerManager;
import net.qoopo.framework.exception.NullArgumentException;
import net.qoopo.framework.multitenant.MultitenantConfigurer;
import net.qoopo.framework.services.QoopoService;
import net.qoopo.framework.tasks.QoopoTask;

/**
 * QoopoFramework es un conjunto de rutinas y utilitarios que serán utilizados
 * en las aplicaciones Qoopo
 * 
 * Features:
 * 
 * - Acceso al controlador Jpa para el acceso a la base de datos GenericBusiness
 * -
 * -
 * 
 * @param datasourceName
 * @return
 */
@Getter
@Setter
// @Builder
// @AllArgsConstructor(access = AccessLevel.PACKAGE)
public class QoopoFramework {

    private static QoopoFramework INSTANCE = null;

    public static final String version = "1.0.0-beta";

    private String dataSourceName;

    private MultitenantConfigurer multitenantConfigurer=new MultitenantConfigurer();

    private QoopoFramework() {
        //
    }

    public static QoopoFramework get() {
        if (INSTANCE == null) {
            INSTANCE = new QoopoFramework();
        }
        return INSTANCE;
    }

    public QoopoFramework dataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

   
    public QoopoFramework multitenancy(Supplier<MultitenantConfigurer> config) {
        if (config == null)
            throw new NullArgumentException();
        this.multitenantConfigurer = config.get();
        return this;
    }

    public QoopoFramework multitenancy(Function<MultitenantConfigurer, MultitenantConfigurer> config) {
        if (config == null)
            throw new NullArgumentException();
        this.multitenantConfigurer = config.apply(multitenantConfigurer);
        return this;
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

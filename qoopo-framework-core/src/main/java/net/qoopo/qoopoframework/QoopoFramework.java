package net.qoopo.qoopoframework;

import lombok.Getter;
import lombok.Setter;

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
public class QoopoFramework {

    private static QoopoFramework INSTANCE = null;

    private String dataSourceName;

    public static final String version = "1.0.0-beta";

    public static QoopoFramework get() {
        if (INSTANCE == null) {
            INSTANCE = new QoopoFramework();
        }
        return INSTANCE;
    }

    /**
     * Inicializa el framework con los parámetros necesarios como el nombre del
     * datasource
     * 
     * @param datasourceName
     * @return
     */
    public QoopoFramework datasource(String datasourceName) {
        this.dataSourceName = datasourceName;
        return this;
    }
}

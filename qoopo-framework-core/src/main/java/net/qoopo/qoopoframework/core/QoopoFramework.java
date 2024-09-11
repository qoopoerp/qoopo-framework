package net.qoopo.qoopoframework.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QoopoFramework {

    private static QoopoFramework INSTANCE = null;

    private String dataSourceName;

    public static QoopoFramework get() {
        if (INSTANCE == null) {
            INSTANCE = new QoopoFramework();
        }
        return INSTANCE;
    }

    public QoopoFramework datasource(String datasourceName) {
        this.dataSourceName = datasourceName;
        return this;
    }
}

package net.qoopo.framework.web.providers;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.jpa.core.EntidadBase;

@Getter
@Setter
public abstract class DataProvider<T extends EntidadBase> implements Serializable {

    protected boolean cacheable = false;

    protected Long lifeCacheMilis = 3000L;
    protected Long lastLoad = 0L;

    public abstract void loadData();

    protected Iterable<T> data = new ArrayList<>();

    public Iterable<T> getData() {
        // implementar manejo de cache, en el cual se pregunta el tiempo desde la ultima
        // carga para vovler a cargar
        if (!cacheable)
            loadData();
        else {
            if (System.currentTimeMillis() - lastLoad > lifeCacheMilis) {
                loadData();
                lastLoad = System.currentTimeMillis();
            }
        }
        return data;
    }
}

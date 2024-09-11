package net.qoopo.qoopoframework.web.providers;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.qoopoframework.core.db.core.base.EntidadBase;
import net.qoopo.qoopoframework.web.AppSessionBeanInterface;

@Getter
@Setter
public abstract class DataProvider<T extends EntidadBase> implements Serializable {

    @Inject
    protected AppSessionBeanInterface sessionBean;

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

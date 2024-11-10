package net.qoopo.framework.security.web.repository;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class HttpRepository<T> {

    private static Logger log = Logger.getLogger("http-Repository");
    private String httpAttributeName;
    private boolean allowCreateSession = false;

    public HttpRepository(String httpAttributeName) {
        this.httpAttributeName = httpAttributeName;
    }

    public HttpRepository(String httpAttributeName, boolean allowCreateSession) {
        this.httpAttributeName = httpAttributeName;
        this.allowCreateSession = allowCreateSession;
    }

    public void save(T context, HttpWarehouse warehouse) {
        HttpSession session = warehouse.getRequest().getSession(allowCreateSession);
        if (session != null) {
            log.info("Si existe una sesión, se cuarda el atributo, sesion id=" + session.getId());
            session.setAttribute(httpAttributeName, context);
        } else {
            log.info("No existe una sesión no se guarda nada");
        }
    }

    public T load(HttpWarehouse warehouse) {
        HttpSession session = warehouse.getRequest().getSession(false);
        if (session != null) {
            log.info("Si hay una sesión , se carga el atributo, sesión id=" + session.getId());
            Object value = session.getAttribute(httpAttributeName);
            if (value != null)
                return (T) value;
        } else {
            log.warning("Cargando, no hay una sesion");
        }
        log.info("No se carga nada del atributo");
        return null;
    }
}

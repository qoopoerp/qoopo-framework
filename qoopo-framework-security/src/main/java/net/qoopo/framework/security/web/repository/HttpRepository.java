package net.qoopo.framework.security.web.repository;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.config.SecurityConfig;

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
            if (SecurityConfig.get().isDebug())
                log.info("[+] Guardando en la sesion id=" + session.getId());
            session.setAttribute(httpAttributeName, context);
        } else {
            if (SecurityConfig.get().isDebug())
                log.info("No se guardó nada");
        }
    }

    public T load(HttpWarehouse warehouse) {
        HttpSession session = warehouse.getRequest().getSession(false);
        if (session != null) {
            if (SecurityConfig.get().isDebug())
                log.info("[+] Cargando de sesión existente id=" + session.getId());
            Object value = session.getAttribute(httpAttributeName);
            if (value != null)
                return (T) value;
        } else {
            if (SecurityConfig.get().isDebug())
                log.warning("[+] Cargando, no hay una sesion");
        }
        if (SecurityConfig.get().isDebug())
            log.info("No se cargó nada");
        return null;
    }
}

package net.qoopo.qoopoframework.router;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author noroot
 */
@Getter
@Setter
public class QRoute {

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_WEBSITE = 1;
    public static final int TYPE_BACKEND = 2;

    private String route;
    private String page;
    private boolean requireSession = false;
    // ademas de requerir sesion, puede ser que una pagina no requiera que tenga un
    // permiso en la tabla de permisos, este es el caso de paginas como inicio y
    // paginas de errores,
    // todos tienen permiso por default y no se debe especificar en ningun modulo
    private boolean requirePermission = false;
    private int type;
    private boolean exactRoute = false;

    public static final QRoute INVALID_ROUTE = new QRoute("404.jsf", "404.jsf", false, TYPE_INVALID, true);

    public static QRoute build(String route, String page, boolean requireSession, int type, boolean exactRoute) {
        return new QRoute(route, page, requireSession, type, exactRoute);
    }

    public static QRoute build(String route, String page, boolean requireSession, boolean requirePermission, int type,
            boolean exactRoute) {
        return new QRoute(route, page, requireSession, requirePermission, type, exactRoute);
    }

    public QRoute() {
        //
    }

    public QRoute(String route, String page, boolean requireSession, int type) {
        this.route = route;
        this.page = page;
        this.requireSession = requireSession;
        this.type = type;
    }

    public QRoute(String route, String page, boolean requireSession, boolean requirePermission, int type) {
        this.route = route;
        this.page = page;
        this.requireSession = requireSession;
        this.requirePermission = requirePermission;
        this.type = type;
    }

    public QRoute(String route, String page, boolean requireSession, int type, boolean exactRoute) {
        this.route = route;
        this.page = page;
        this.requireSession = requireSession;
        this.type = type;
        this.exactRoute = exactRoute;
    }

    public QRoute(String route, String page, boolean requireSession, boolean requirePermission, int type,
            boolean exactRoute) {
        this.route = route;
        this.page = page;
        this.requireSession = requireSession;
        this.requirePermission = requirePermission;
        this.type = type;
        this.exactRoute = exactRoute;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.route);
        hash = 89 * hash + Objects.hashCode(this.page);
        hash = 89 * hash + (this.requireSession ? 1 : 0);
        hash = 89 * hash + this.type;
        hash = 89 * hash + (this.exactRoute ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QRoute other = (QRoute) obj;
        if (this.requireSession != other.requireSession) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.exactRoute != other.exactRoute) {
            return false;
        }
        if (!Objects.equals(this.route, other.route)) {
            return false;
        }
        return Objects.equals(this.page, other.page);
    }

    @Override
    public String toString() {
        return "QRoute{" + "route=" + route + ", page=" + page + ", requireSession=" + requireSession + ", type=" + type
                + ", exactRoute=" + exactRoute + '}';
    }

}

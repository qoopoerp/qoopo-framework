package net.qoopo.framework.router;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una ruta de la aplicación
 * 
 * TYPE_INVALID - La ruta es invalida
 * TYPE_PUBLIC - La ruta es publica y no requiere estar autenticado para acceder
 * TYPE_WEBSITE - La ruta es parte del acceso web, en caso de no estar
 * autenticado se redirige hacia el home
 * 
 * @author Alberto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class QRoute {

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_WEBSITE = 1;
    public static final int TYPE_BACKEND = 2;

    private String route;
    private String page;
    @Builder.Default
    private boolean requireSession = false;
    // ademas de requerir sesion, puede ser que una pagina no requiera que tenga un
    // permiso en la tabla de permisos, este es el caso de paginas como inicio y
    // paginas de errores,
    // todos tienen permiso por default y no se debe especificar en ningun modulo
    @Builder.Default
    private boolean requirePermission = false;
    private int type;
    @Builder.Default
    private boolean exactRoute = false;

    public static final QRoute INVALID_ROUTE = QRoute.build("404.jsf", "404.jsf", false, TYPE_INVALID, true);

    public static QRoute build(String page, boolean requireSession, int type, boolean exactRoute) {
        // return new QRoute(page, requireSession, type, exactRoute);
        return QRoute.builder().page(page).route(page).requireSession(requireSession).type(type).exactRoute(exactRoute)
                .build();
    }

    public static QRoute build(String page, boolean requireSession, boolean requirePermission, int type,
            boolean exactRoute) {
        return QRoute.builder().page(page).route(page).requireSession(requireSession)
                .requirePermission(requirePermission).type(type).exactRoute(exactRoute).build();
    }

    public static QRoute build(String route, String page, boolean requireSession, int type, boolean exactRoute) {
        return QRoute.builder().page(page).route(route).requireSession(requireSession).type(type).exactRoute(exactRoute)
                .build();
    }

    public static QRoute build(String route, String page, boolean requireSession, boolean requirePermission, int type,
            boolean exactRoute) {
        return QRoute.builder().page(page).route(route).requireSession(requireSession)
                .requirePermission(requirePermission).type(type).exactRoute(exactRoute).build();
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

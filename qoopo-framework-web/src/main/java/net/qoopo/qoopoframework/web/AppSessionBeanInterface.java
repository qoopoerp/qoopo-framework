package net.qoopo.qoopoframework.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.CoreMetadata;
import net.qoopo.qoopoframework.core.db.core.base.interfaces.CoreUser;

/**
 * Esta interface debe ser implementada por el bean que da información de la
 * sesión actual como:
 * Usuario
 *
 * @author ALBERTO
 */
@Named
@ApplicationScoped
public interface AppSessionBeanInterface {

    public CoreUser getUser();

    public String getUserName();

    public CoreMetadata agregarEvento(CoreMetadata datos, String evento);

    public CoreMetadata agregarCreacion(CoreMetadata datos);

    public CoreMetadata agregarEdicion(CoreMetadata datos);

    public void addUrlParam(String param, String value);

    public void removeUrlParam(String param);

    public String getZonaHoraria();

    public Long getEmpresaId();
}

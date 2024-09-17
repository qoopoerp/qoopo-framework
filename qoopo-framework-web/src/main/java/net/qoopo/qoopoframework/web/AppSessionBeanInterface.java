package net.qoopo.qoopoframework.web;

import net.qoopo.qoopoframework.jpa.core.interfaces.CoreMetadata;
import net.qoopo.qoopoframework.jpa.core.interfaces.CoreUser;

/**
 * Esta interface debe ser implementada por el bean que da información de la
 * sesión actual como:
 * Usuario
 *
 * @author ALBERTO
 */
// @Named
// @ApplicationScoped
public interface AppSessionBeanInterface {

    public CoreUser getUser();

    public String getUserName();

    public CoreMetadata addEvent(CoreMetadata datos, String evento);

    public CoreMetadata addCreatedEvent(CoreMetadata datos);

    public CoreMetadata addEditedEvent(CoreMetadata datos);

    public void addUrlParam(String param, String value);

    public void removeUrlParam(String param);

    public String getZonaHoraria();

    public Long getEmpresaId();

    public boolean isLogged();

    public boolean isAllowed(String page);

    public boolean isAllowed(String page, String params);

    public boolean isAllowed(String page, String params, boolean abrir);

    public void reloadPermissions();
}

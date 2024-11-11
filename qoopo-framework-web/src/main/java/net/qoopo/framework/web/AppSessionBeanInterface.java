package net.qoopo.framework.web;

import net.qoopo.framework.jpa.core.interfaces.CoreMetadata;

/**
 * Esta interface debe ser implementada por el bean que da información de la
 * sesión actual como:
 * Usuario
 *
 * @author ALBERTO
 */

public interface AppSessionBeanInterface {

    public CoreMetadata addEvent(CoreMetadata datos, String evento);

    public CoreMetadata addCreatedEvent(CoreMetadata datos);

    public CoreMetadata addEditedEvent(CoreMetadata datos);

    public void addUrlParam(String param, String value);

    public void removeUrlParam(String param);

}

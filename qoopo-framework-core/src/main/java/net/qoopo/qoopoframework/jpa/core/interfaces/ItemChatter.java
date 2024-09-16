package net.qoopo.qoopoframework.jpa.core.interfaces;

import java.time.LocalDateTime;

/**
 * Esta interfaz es para las entidades que puedan ser insertadas en el chatter
 *
 * @author alberto
 */
public interface ItemChatter extends Auditable{

    public String getTitle();

    public LocalDateTime getDate();

    public String getBody();

    public CoreUser getUser();

    public int getTipoItem();

    public CoreUser getUser2();
}

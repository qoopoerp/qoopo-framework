package net.qoopo.framework.jpa.core.interfaces;

/**
 * Esta interfaz es para las entidades que pueden ser archivadas
 *
 * @author alberto
 */
public interface Archivable {

    public Boolean getArchived();

    public void setArchived(Boolean archived);

}

package net.qoopo.qoopoframework.jpa.core.interfaces;

/**
 * Debe ser implementada por la entidad que representa un usuario del sistema
 */
public interface CoreUser {
    public String getName();

    public String getFullName();

    public String getUser();

    public String getPasswordHash();
}

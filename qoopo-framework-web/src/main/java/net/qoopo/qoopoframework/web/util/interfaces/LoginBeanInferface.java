package net.qoopo.qoopoframework.web.util.interfaces;

import net.qoopo.qoopoframework.jpa.core.interfaces.CoreUser;

public interface LoginBeanInferface {

    public CoreUser getUser();

    public void setUser(CoreUser user);

    public boolean isLogueado();

    public void setLogueado(boolean logueado);

    public void recargarUsuario();
}

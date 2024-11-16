package net.qoopo.framework.web.template;

import java.util.List;

/**
 * Se encarga de mostrar las aplicaciones instaladas y que se tiene permiso
 * La aplicacion debe crear un Bean lamado AppListBean que implemente este
 * interface
 */
public interface IAppListBean {

    public List<Application> getAppList();

    public void openApp(Application application);
}

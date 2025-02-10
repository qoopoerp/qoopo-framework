package net.qoopo.framework.web.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.faces.FactoryFinder;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.el.ValueBinding;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * JSF utilities.
 */
public class FacesUtils {

    public static final Logger log = Logger.getLogger("FacesUtils");

    public static void redirect(String page) {
        try {
            log.log(Level.FINE, "Redireccionando a:{0}", page);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            String contextPath = externalContext.getRequestContextPath();
            externalContext.redirect(contextPath + page);
            facesContext.responseComplete();
        } catch (IOException ex) {
            // log.log(Level.SEVERE, "Error al redireccionar [" + page + "]", ex);
        }
    }

    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    // public static Object getManagedBean(String beanName) {
    // // SomeBean bc = context.getApplication().evaluateExpressionGet(context,
    // // "#{someBean}", SomeBean.class)
    // return
    // getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());
    // }
    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */

    // public static void resetManagedBean(String beanName) {
    // getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(),
    // null);
    // }
    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName    the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static Object getRequestParameterObjeto(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        FacesUtils.addErrorMessage(null, msg);

    }

    /**
     * Add error message por medio de exception
     *
     * @param ex
     */
    public static void addErrorMessage(Exception ex) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Mensaje: Informe al Administrador del Sistema: [" + ex.getLocalizedMessage() + "]", ""));
        // QoopoUtil.print(ex)+")",QoopoUtil.print(ex)));
        ex.printStackTrace();
        log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    }

    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
        log.severe("Error -> " + msg);
    }

    public static void addErrorMessageException(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            FacesUtils.addErrorMessage(msg);
        } else {
            FacesUtils.addErrorMessage(defaultMsg);
        }
    }

    /**
     * Add warning message.
     *
     * @param msg the warning message
     */
    public static void addWarningMessage(String msg) {
        FacesUtils.addWarningMessage(null, msg);
    }

    /**
     * Add warning message por medio de exception
     *
     * @param ex
     */
    public static void addWarningMessage(Exception ex) {
        FacesUtils.addWarningMessage(null, ex.getMessage());

    }

    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the error message
     */
    public static void addWarningMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
        log.warning(msg);
    }

    private static Application getApplication() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        return appFactory.getApplication();
    }

    // private static ValueBinding getValueBinding(String el) {
    // return getApplication().createValueBinding(el);
    // }
    // private static String getJsfEl(String value) {
    // return "#{" + value + "}";
    // }
    public static String usuarioLogueado() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getUserPrincipal().getName();
    }

    private FacesUtils() {
        // constructor archivo vacio
    }
}

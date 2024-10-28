package net.qoopo.framework.web.util;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

public class Informacion {

    public static String getLogoQoopo() {
        return directorioActual() + "resources/imagenes/logo.png";
    }

    public static String getLocalIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception ex) {
            Logger.getLogger(Informacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "n/d";
    }

    public static String getLocalHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (Exception ex) {
            Logger.getLogger(Informacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "n/d";
    }

    public static String getReportsPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes");
    }

    public static String directorioActual() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                .getContext();
        return servletContext.getRealPath("/"); // Sustituye "/" por el directorio
    }

    private Informacion() {
        //
    }

}

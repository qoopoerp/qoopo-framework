package net.qoopo.framework.web.vistas;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import net.qoopo.framework.lang.LanguageProvider;
import net.qoopo.framework.reports.Reporte;
import net.sf.jasperreports.engine.JRException;

/**
 * Este bean maneja la pantalla del gestor de repotes del ERP
 *
 * @author ALBERTO
 */
@Named
@SessionScoped
public class ReporteBean implements Serializable {

    public static final Logger log = Logger.getLogger("Qoopo");

    private Reporte reporte;

    // @Inject
    // private AppSessionBeanInterface sessionBean;

    // @Inject
    // protected LanguageProvider languageProvider;

    public ReporteBean() {
        //
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public void descargarPDF() {
        try {
            reporte.addParam("showHeader", Boolean.TRUE);
            reporte.generarPdf();
            reporte.generarPdfStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dscargarXLS() {
        try {
            reporte.addParam("showHeader", Boolean.FALSE);
            reporte.generarXLSXStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarODT() {
        try {
            reporte.addParam("showHeader", Boolean.TRUE);
            reporte.generarODTStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarDOCX() {
        try {
            reporte.addParam("showHeader", Boolean.TRUE);
            reporte.generarDOCXStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarODS() {
        try {
            reporte.addParam("showHeader", Boolean.TRUE);
            reporte.generarODSStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarXML() {
        try {
            reporte.addParam("showHeader", Boolean.FALSE);
            reporte.generarXMLStream();
        } catch (JRException | FileNotFoundException ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMail() {

        // sessionBean.getEnviarMail().limpiarAdjuntos();
        // sessionBean.getEnviarMail().limpiarDestinatarios();
        // sessionBean.getEnviarMail().addAdjuntos(new Adjunto(reporte.getNombre() +
        // ".pdf", reporte.getOutputFile(),
        // "" + (new File(reporte.getOutputFile()).length() / 1024) + " Kb"));
    }

    /**
     * Enviar el reporte pdf generado al módulo de documentos
     */
    public void enviarDocumentos() {
        /*
         * try {
         * 
         * Archivo archivo = new Archivo();
         * archivo.setEmpresa(sessionBean.getEmpresa());
         * archivo.setPropietario(sessionBean.getUsuario());
         * archivo.setNombre(reporte.getNombre());
         * if (!reporte.getNombre().endsWith(".pdf")) {
         * archivo.setNombre(reporte.getNombre() + ".pdf");
         * }
         * archivo.setPrivacidad(Archivo.PRIVACIDAD_PRIVADO);
         * archivo.setContentType("application/pdf");
         * archivo.setFecha(LocalDateTime.now());
         * archivo.setFechaModificacion(LocalDateTime.now());
         * archivo.setContenido(new ArchivoContenido(archivo,
         * FileUtil.readAllBytes(reporte.getOutputFile())));
         * archivo.setTamanio((long) archivo.getContenido().getDatos().length);
         * GenericBusiness.create(archivo);
         * FacesUtils.addInfoMessage(textoBean.texto(909));
         * } catch (Exception e) {
         * FacesUtils.addErrorMessage(textoBean.texto(910));
         * log.log(Level.SEVERE, e.getMessage(), e);
         * }
         */
    }

    /**
     * Devuelve la ruta del archivo de salida del reporte generado
     *
     * @return
     */
    public String getContextPath() {
        try {
            if (reporte != null) {
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                        .getContext();
                // Del reporte el outputfile es la ruta absoluta en el servidor, por lo tanto
                // debemos tomar esa ruta y eliminar ar la ruta del contexto para obtener la
                // ruta relativa
                return reporte.getOutputFile().replace(servletContext.getRealPath("/"), "/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

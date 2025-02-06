package net.qoopo.framework.reports;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author aigarcia
 */
@Getter
@Setter
public class Reporte {

    private byte[] contentBytes = null;
    private String nombreReporte = null;
    private String nombre;
    private String tipo;
    private String outputFile;
    private HashMap<String, Object> parameters;
    private StreamedContent contenido;
    private List source;
    private GestorReporte gestor;

    /*
     * private static File getRealPath(String recurso) {
     * ServletContext servletContext = (ServletContext)
     * FacesContext.getCurrentInstance().getExternalContext().getContext();
     * String salida = servletContext.getRealPath(recurso);
     * return new File(salida);
     * }
     * 
     * public Reporte(String nombreReporte, String nombre) {
     * this.nombreReporte = nombreReporte;
     * this.nombre = nombre;
     * gestor = new GestorReporte(getRealPath("").getAbsolutePath());
     * //agrego el JpaParameters SUBREPORT_DIR que es el mismo para todos los reportes,
     * la ruta de los reportes
     * addParam("SUBREPORT_DIR", gestor.getRealPath("/") + "/");
     * // addParam("SUBREPORT_DIR", reportsPath + "/");
     * }
     */

    public Reporte(String nombreReporte, String nombre, String reportsPath) {
        this.nombreReporte = nombreReporte;
        this.nombre = nombre;
        gestor = new GestorReporte(reportsPath);
        // agrego el JpaParameters SUBREPORT_DIR que es el mismo para todos los reportes, la
        // ruta de los reportes
        addParam("SUBREPORT_DIR", gestor.getRealPath("/") + "/");
        // addParam("SUBREPORT_DIR", reportsPath + "/");
    }

    public Reporte(String nombreReporte, String nombre, String tipo, HashMap<String,Object> parameters, String reportsPath) {
        this.nombreReporte = nombreReporte;
        this.nombre = nombre;
        this.tipo = tipo;
        this.parameters = parameters;
        gestor = new GestorReporte(reportsPath);
        // agrego el JpaParameters SUBREPORT_DIR que es el mismo para todos los reportes, la
        // ruta de los reportes
        addParam("SUBREPORT_DIR", gestor.getRealPath("/") + "/");
        // addParam("SUBREPORT_DIR", reportsPath + "/");
    }

    public Reporte(byte[] jasperContentBytes, String nombre, String reportsPath) {
        this.contentBytes = jasperContentBytes;
        this.nombre = nombre;
        gestor = new GestorReporte(reportsPath);
        // agrego el JpaParameters SUBREPORT_DIR que es el mismo para todos los reportes, la
        // ruta de los reportes
        addParam("SUBREPORT_DIR", gestor.getRealPath("/") + "/");
        // addParam("SUBREPORT_DIR", reportsPath + "/");
    }

    public void addParam(String JpaParameters, Object valor) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(JpaParameters, valor);
    }

    public void generarPdf() throws JRException, FileNotFoundException {
        outputFile = gestor.exportPDF(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarPdfStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportPDFStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarXLSX() throws JRException, FileNotFoundException {
        outputFile = gestor.exportXLSX(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarXLSXStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportXLSXStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarDOCX() throws JRException, FileNotFoundException {
        outputFile = gestor.exportDOCX(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarDOCXStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportDOCXStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarPPTX() throws JRException, FileNotFoundException {
        outputFile = gestor.exportPPTX(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarPPTXStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportPPTXStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarODT() throws JRException, FileNotFoundException {
        outputFile = gestor.exportODT(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarODTStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportODTStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarODS() throws JRException, FileNotFoundException {
        outputFile = gestor.exportODS(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarODSStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportODSStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    public void generarXML() throws JRException, FileNotFoundException {
        outputFile = gestor.exportXML(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
    }

    public StreamedContent generarXMLStream() throws JRException, FileNotFoundException {
        contenido = gestor.exportXMLStream(nombreReporte, contentBytes, parameters, nombre,
                new JRBeanCollectionDataSource(source));
        return contenido;
    }

    // public String getRutaArchivoSalida(, String reportsPath) {
    // return
    // FacesContext.getCurrentInstance().getExternalContext().getRealPath(outputFile);
    // }
}

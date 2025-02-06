/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.framework.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
public class GestorReporte {

//    public final String REPORT_PATH = "/reportes/";
//    public final String REPORT_PATH = "/";
    public final String REPORT_EXPORT_PATH = "/resources/";
//    private boolean metodoConexionFijo = false;
    private String EXT = ".jasper";

    /**
     * La ruta donde se encuentran los reportes
     */
    private String reportsPath = "";

    public GestorReporte(String reportsPath) {
        this.reportsPath = reportsPath;
        try {
            getRealPath(REPORT_EXPORT_PATH).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getRealPath(String recurso) {
//        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//        String salida = servletContext.getRealPath(recurso);
//        return new File(salida);
        return new File(reportsPath, recurso);
    }

    public InputStream getStream(String recurso) throws FileNotFoundException {
        File reporte = new File(getRealPath("/"), recurso);
        if (reporte.exists()) {
            return new FileInputStream(reporte);
        } else {
            return GestorReporte.class.getResourceAsStream("/src/main/resources/META-INF/resources/" + recurso);
        }
    }

    public InputStream getStream(byte[] contentBytes) {
        return new ByteArrayInputStream(contentBytes);
    }

    private Connection conexion() {
        Connection conn = null;
//        InitialContext initialContext;
//        try {
//            if (metodoConexionFijo) {
//                // metodo cuando busco el DataSource en el glassfish
//                initialContext = new InitialContext();
//                DataSource ds = (DataSource) initialContext.lookup("jdbc/qoopods");//            
//                conn = ds.getConnection();
//            } else {
//                //*******************************************************************************
//                // Otro metodo donde consigo la misma conexion del entitymanager                
//                EntityManager em;
//                em = Transaccion.getEntityManager();
//                em.getTransaction().begin();
//                conn = em.unwrap(java.sql.Connection.class);
//                em.getTransaction().commit();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(GestorReporte.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return conn;
    }

    private String getArchivoSalida(String nombre, String extension) {
        return getRealPath(REPORT_EXPORT_PATH).getAbsolutePath() + "/" + nombre + extension;
    }

    private JasperPrint print(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperReport report;
        if (contentBytes == null) {
            report = (JasperReport) JRLoader.loadObject(getStream(reporte + EXT));
        } else {
            report = (JasperReport) JRLoader.loadObject(getStream(contentBytes));
        }
        // Rellenamos el informe con la conexion creada y sus parameters establecidos
        JasperPrint print = null;
        if (dataSource != null) {
            print = JasperFillManager.fillReport(report, map, dataSource);
        } else {
            print = JasperFillManager.fillReport(report, map, conexion());
        }
        return print;
    }

    public String exportPDF(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        // Exportamos el informe a formato PDF
        JasperExportManager.exportReportToPdfFile(print, getArchivoSalida(nombre, ".pdf"));
        return getArchivoSalida(nombre, ".pdf");
    }

    public StreamedContent exportPDFStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JasperExportManager.exportReportToPdfStream(print, out);  // Exportamos el informe a formato PDF
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/pdf").name(nombre + ".pdf").build();
    }

    public String exportXLSX(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        // Exportamos el informe a formato XLS
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getArchivoSalida(nombre, ".xlsx")));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setCollapseRowSpan(false);
        configuration.setDetectCellType(true);
        configuration.setIgnoreCellBorder(true);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return getArchivoSalida(nombre, ".xlsx");

    }

    public StreamedContent exportXLSXStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setCollapseRowSpan(false);
        configuration.setDetectCellType(true);
        configuration.setIgnoreCellBorder(true);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").name(nombre + ".xlsx").build();
    }

    public String exportDOCX(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getArchivoSalida(nombre, ".docx")));
        exporter.exportReport();
        return getArchivoSalida(nombre, ".docx");
    }

    public StreamedContent exportDOCXStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document").name(nombre + ".docx").build();
    }

    public String exportPPTX(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getArchivoSalida(nombre, ".pptx")));
        exporter.exportReport();
        return getArchivoSalida(nombre, ".pptx");
    }

    public StreamedContent exportPPTXStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/vnd.openxmlformats-officedocument.presentationml.presentation").name(nombre + ".pptx").build();
    }

    public String exportODT(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JROdtExporter exporter = new JROdtExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getArchivoSalida(nombre, ".odt")));
        exporter.exportReport();
        return getArchivoSalida(nombre, ".odt");

    }

    public StreamedContent exportODTStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JROdtExporter exporter = new JROdtExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/vnd.oasis.opendocument.text").name(nombre + ".odt").build();
    }

    public String exportODS(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JROdsExporter exporter = new JROdsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getArchivoSalida(nombre, ".ods")));
        exporter.exportReport();
        return getArchivoSalida(nombre, ".ods");
    }

    public StreamedContent exportODSStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        JROdsExporter exporter = new JROdsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("application/vnd.oasis.opendocument.spreadsheet").name(nombre + ".ods").build();
    }

    public String exportXML(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        // Exportamos el informe a formato PDF
        JasperExportManager.exportReportToXmlFile(print, getArchivoSalida(nombre, ".xml"), true);
        return getArchivoSalida(nombre, ".xml");
    }

    public StreamedContent exportXMLStream(String reporte, byte[] contentBytes, Map<String, Object> map, String nombre, JRBeanCollectionDataSource dataSource) throws JRException, FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //Obtenemos el reporte compilado
        JasperPrint print = print(reporte, contentBytes, map, nombre, dataSource);
        // Exportamos el informe a formato PDF
        JasperExportManager.exportReportToXmlStream(print, out);
        return DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray())).contentType("text/xml").name(nombre + ".xml").build();
    }
}

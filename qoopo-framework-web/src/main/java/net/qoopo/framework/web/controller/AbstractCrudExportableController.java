package net.qoopo.framework.web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.exporter.Exportable;
import net.qoopo.framework.exporter.Exporter;
import net.qoopo.framework.exporter.Importer;
import net.qoopo.framework.exporter.csv.CsvExporter;
import net.qoopo.framework.exporter.csv.CsvImporter;
import net.qoopo.framework.exporter.json.JsonExporter;
import net.qoopo.framework.exporter.json.JsonImporter;
import net.qoopo.framework.exporter.xls.XlsExporter;
import net.qoopo.framework.exporter.xls.XlsImporter;
import net.qoopo.framework.exporter.xlsx.XlsxExporter;
import net.qoopo.framework.exporter.xlsx.XlsxImporter;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Controlador web para JSF que gestiona el proceso CRUD de entidades que
 * heredan de AbstractEntity
 */
@Getter
@Setter
public abstract class AbstractCrudExportableController<Entity, EntityData, EntityID>
        extends AbstractCrudController<Entity, EntityData, EntityID> {

    protected transient StreamedContent contenidoExportar;
    protected transient InputStream inputArchivoImport;
    protected Exporter exporter = new CsvExporter();
    protected Importer importer = new CsvImporter();
    protected int importerType = 1;

    /**
     * Se puede establecer el nombre del archivo a exportar, en caso de no definirlo
     * se usa el nombre de la clase de la entidad
     */
    protected String exportedNameFile = null;
    protected int exporterType = 1;

    public abstract void importar();

    public void updateExporter() {
        log.info("actualizando exporter " + exporterType);
        switch (exporterType) {
            case 1:
                exporter = new CsvExporter();
                break;
            case 2:
                exporter = new XlsExporter();
                break;
            case 3:
                exporter = new XlsxExporter();
                break;
            case 4:
                exporter = new JsonExporter();
                break;
        }
    }

    public void updateImporter() {
        log.info("actualizando importer " + importerType);
        switch (importerType) {
            case 1:
                importer = new CsvImporter();
                break;
            case 2:
                importer = new XlsImporter();
                break;
            case 3:
                importer = new XlsxImporter();
                break;
            case 4:
                importer = new JsonImporter();
                break;
        }
    }

    public void exportar() {
        log.warning("[!] Exportando [" + getClass().getName() + "]");
        try {
            // exportar(exporter, data, getClass().getSimpleName() + ".csv");
            String fileName = null;
            if (exportedNameFile != null)
                fileName = exportedNameFile;
            else
                fileName = data.iterator().next().getClass().getSimpleName();
            exportar(exporter, data, fileName);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Error al exportar " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void exportar(Exporter exporter, Iterable<EntityData> data, String nombre) {
        try {
            exporter.clear();
            byte[] datos;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            exporter.setOutputExporter(bos);
            for (Object oitem : data) {
                if (oitem instanceof Exportable) {
                    Exportable item = (Exportable) oitem;
                    exporter.startItem();
                    item.exportar(exporter);
                    exporter.endItem();
                }
            }
            exporter.exportar();
            datos = bos.toByteArray();
            bos.close();
            InputStream is = new ByteArrayInputStream(datos);
            contenidoExportar = DefaultStreamedContent.builder().contentType(exporter.getMimetype())
                    .name(nombre + exporter.getExtension())
                    .stream(() -> is).build();
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Error al exportar " + ex.getMessage());
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}

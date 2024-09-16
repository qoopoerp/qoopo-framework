package net.qoopo.qoopoframework.web.vistas;

import org.primefaces.event.FileUploadEvent;

import net.qoopo.qoopoframework.jpa.core.interfaces.Auditable;
import net.qoopo.qoopoframework.jpa.core.interfaces.CoreMetadata;
import net.qoopo.qoopoframework.jpa.core.interfaces.CoreMetadataActividad;
import net.qoopo.qoopoframework.jpa.core.interfaces.CoreMetadataFile;
import net.qoopo.qoopoframework.jpa.core.interfaces.CoreMetadataNote;
import net.qoopo.qoopoframework.jpa.core.interfaces.ItemChatter;

/**
 * Interface para el bean bean maneja la pantalla que muestra los metadatos
 *
 * @author ALBERTO
 */
public interface ChatterInterface {

    public void save();

    public CoreMetadata getObjeto();

    public void loadProperties();

    public void saveProperties();

    public void saveProperties(boolean save);

    public void setObjeto(CoreMetadata objeto);

    public void mostrar(CoreMetadata metaData, Auditable auditable);

    public void subirArchivo(FileUploadEvent event);

    public void deleteFile(CoreMetadataFile item);

    public void eliminarNota(CoreMetadataNote item);

    public void agregarNota();

    public void eliminarActividad(CoreMetadataActividad item);

    public void nuevaActividad();

    public void seleccionarTipoActividad();

    public void agregarActividad();

    public void marcarHecho(CoreMetadataActividad actividad);

    public void deleteItem(ItemChatter item);

    public void sendMail();

}

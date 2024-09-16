package net.qoopo.qoopoframework.jpa.core.interfaces;

import java.util.List;

/**
 * Debe ser implementada por la entidad que representa a los metadaos que será
 * mostrados por el chatter
 * 
 */
public interface CoreMetadata extends EntityId {

    // public Long getId();

    public void setUser(CoreUser user);

    public List<CoreMetadataActividad> getActividades();

    public void setActividades(List<CoreMetadataActividad> value);

    public List<CoreMetadataAuditoria> getAuditorias();

    public void setAuditorias(List<CoreMetadataAuditoria> value);

    public List<CoreMetadataFile> getArchivos();

    public void setArchivos(List<CoreMetadataFile> value);

    public void setObjetoUrl(String url);

    public void addFile(CoreMetadataFile file);

    public void addAuditoria(CoreMetadataAuditoria value);

    public void addActividad(CoreMetadataActividad value);

    public void addNote(CoreMetadataNote value);

    public void removeFile(CoreMetadataFile file);

    public void removeAuditoria(CoreMetadataAuditoria value);

    public void removeActividad(CoreMetadataActividad value);

    public void removeNote(CoreMetadataNote value);

}

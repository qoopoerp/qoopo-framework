package net.qoopo.framework.jpa.core.interfaces;

public interface Auditable {

    public CoreMetadata getMetadato();

    public void setMetadato(CoreMetadata metadato);

    public String getRecipients();
}

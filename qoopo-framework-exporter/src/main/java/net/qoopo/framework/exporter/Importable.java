package net.qoopo.framework.exporter;

/**
 * Interfaz que indica que un objeto es importable y debe implementar el método
 * importar
 */
public interface Importable {
    public void importar(Importer imp);
}

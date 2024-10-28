package net.qoopo.framework.compressor;

import java.io.File;

/**
 * Realiza la compresión de un arreglo de bytes
 */
public interface ICompressor {

    /*
     * Comprime un arreglo de datos
     */
    public byte[] compress(byte[] data) throws Exception;

    /**
     * Descomprime un arreglo de datos
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] decompress(byte[] data) throws Exception;

    /**
     * Comprime un objeto Java
     * 
     * @param objeto
     * @return
     */
    public byte[] compressObject(Object objeto);

    /**
     * Descomprime un objeto Java
     * 
     * @param bytes
     * @return
     */
    public Object decompressObject(byte[] bytes);

    /**
     * Comprime un archivo
     */
    public void compress(File file) throws Exception;

    /**
     * Comprime un archivo
     * 
     * @param file
     * @param mantenerRuta
     * @throws Exception
     */
    public void compress(File file, boolean mantenerRuta) throws Exception;;

}

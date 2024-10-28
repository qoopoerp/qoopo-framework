package net.qoopo.framework.compressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author alberto
 */
public class GZIPCompressorUtil {

    public static byte[] compressGZIP(byte[] datos) throws Exception {
        byte[] compressed;
        try (ByteArrayOutputStream gzdata = new ByteArrayOutputStream();
                GZIPOutputStream gzipper = new GZIPOutputStream(gzdata);
                ByteArrayInputStream data = new ByteArrayInputStream(datos)) {
            byte[] buffer = new byte[1024];
            int readed;
            while ((readed = data.read(buffer)) > 0) {
                gzipper.write(buffer, 0, readed);
            }
            gzipper.finish();
            compressed = gzdata.toByteArray();
        }
        return compressed;
    }

    public static byte[] decompressGZIP(byte[] datos) throws Exception {
        byte[] returndata = null;
        try (ByteArrayInputStream gzdata = new ByteArrayInputStream(datos);
                GZIPInputStream gunzipper = new GZIPInputStream(gzdata, datos.length);
                ByteArrayOutputStream data = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[1024];
            int readed;
            while ((readed = gunzipper.read(buffer)) > 0) {
                data.write(buffer, 0, readed);
            }
            returndata = data.toByteArray();
        }
        return returndata;
    }

    public static byte[] compress(Object objeto) {
        byte[] bytes = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(baos)) {
            objectOut.writeObject(objeto);
            objectOut.close();
            bytes = compressGZIP(baos.toByteArray());
        } catch (Exception ex) {

        }
        return bytes;
    }

    public static Object decompress(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(decompressGZIP(bytes));
                ObjectInputStream objectIn = new ObjectInputStream(bais)) {
            return (Object) objectIn.readObject();
        } catch (Exception ex) {
        } finally {
        }
        return null;
    }

    private static void compressFile(File file) throws Exception {
        byte[] b = new byte[512];
        File outputFile = new File(file.getParent(), file.getName() + ".zip");
        File tmpFile = new File(outputFile.getAbsolutePath() + ".tmp");
        try (FileOutputStream out = new FileOutputStream(tmpFile);
                ZipOutputStream zout = new ZipOutputStream(out);
                InputStream in = new FileInputStream(file);) {
            zout.putNextEntry(new ZipEntry(file.getName()));
            int len = 0;
            while ((len = in.read(b)) != -1) {
                zout.write(b, 0, len);
            }
            zout.closeEntry();
        }
        outputFile.delete();
        tmpFile.renameTo(outputFile);
    }

    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    public static void compress(final File file, final boolean mantenerRuta) throws Exception {
        new Thread(() -> {
            try {
                if (file.isDirectory()) {
                    compressFiles(getAllFiles(file), new File(file.getParent(), file.getName() + ".zip"), mantenerRuta,
                            file.getParent());
                } else {
                    compressFile(file);
                }
            } catch (Exception e) {
            }
        }).start();
    }

    private static void compressFiles(List<File> files, File outputFile, boolean mantenerRutas, String rutaAeliminar)
            throws Exception {
        File tmpFile = new File(outputFile.getAbsolutePath() + ".tmp");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpFile))) {
            ZipEntry ze;
            rutaAeliminar = rutaAeliminar.replaceAll(Pattern.quote("\\"), "/");
            for (File file : files) {
                String t = file.getAbsolutePath();
                t = t.replaceAll(Pattern.quote("\\"), "/");
                if (mantenerRutas) {
                    try {
                        if (rutaAeliminar.endsWith("/") || rutaAeliminar.endsWith("\\")) {
                            ze = new ZipEntry(t.substring(rutaAeliminar.length()));
                        } else {
                            ze = new ZipEntry(t.substring(rutaAeliminar.length() + 1));
                        }
                    } catch (Exception e) {
                        ze = new ZipEntry(t);
                    }
                } else {
                    ze = new ZipEntry(file.getName());
                }
                zos.putNextEntry(ze);
                // INTRODUCIMOS LOS DATOS DEL FICHERO VACIÓ INTRODUCIDO.
                if (!file.isDirectory()) {
                    // byte[] readAllBytesOfFile = Files.readAllBytes(file.toPath());
                    // zos.write(readAllBytesOfFile, 0, readAllBytesOfFile.length);
                    zos.write(Files.readAllBytes(file.toPath()));
                }
            }
            // CERRAMOS LOS FLUJOS DE DATOS.
            zos.closeEntry();
        }
        outputFile.delete();
        tmpFile.renameTo(outputFile);
    }

    /**
     * Get all files from a directory
     *
     * @param ruta
     * @return
     */
    private static List<File> getAllFiles(File ruta) {
        List<File> r = new ArrayList<>();
        if (ruta.isDirectory()) {
            for (File f2 : ruta.listFiles()) {
                if (f2.isDirectory()) {
                    r.addAll(getAllFiles(f2));
                } else {
                    r.add(f2);
                }
            }
        }
        return r;
    }
}

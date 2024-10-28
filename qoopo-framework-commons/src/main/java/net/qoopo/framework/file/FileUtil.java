
package net.qoopo.framework.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

public class FileUtil {

    /**
     * get todo el archivo en array de strings
     *
     * @param filename
     * @return List<String>
     * @throws java.io.IOException
     */
    public static List<String> readAllLines(String filename) throws IOException {
        Path file = Paths.get(filename);
        return Files.readAllLines(file, Charset.defaultCharset());
    }

    /**
     * get File enviado el path del archivo
     *
     * @param filename
     * @return File
     */
    public static File getFile(String filename) {
        File file = new File(filename);
        return file;
    }

    /**
     * Este metodo valida si existe el archivo si existe retorna true caso
     * contrario retorna false
     *
     * @param filename
     * @return boolean
     */
    public static boolean fileExits(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * Este metodo permite leer el archivo en bytes
     *
     * @param file
     * @return byte[]
     * @throws java.io.IOException
     */
    @SuppressWarnings("resource")
    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * Este metodo permite leer el archivo en bytes
     *
     * @param filename el archivo
     * @return byte[]
     * @throws java.io.IOException
     */
    public static byte[] readAllBytes(String filename) throws IOException {
        Path file = Paths.get(filename);
        return Files.readAllBytes(file);
    }

    /**
     * Delete File
     *
     * @param filename
     * @return boolean
     */
    public static boolean FileDelete(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Mover Archivo
     *
     * @param srFile
     * @param dtFile
     */
    public static void movefile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            f1.delete();
        } catch (FileNotFoundException ex) {
            //System.out.println("MOVER " + ex.getMessage() + " in the specified directory.");
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
    }

    /**
     * Write to file response soap message
     *
     * @param tempFile temp file to create
     * @param response soap message
     * @return void
     */
    public static void writeToFile(File tempFile, SOAPMessage response) throws IOException, SOAPException {
        OutputStream fos = new FileOutputStream(tempFile);
        response.writeTo(fos);
        fos.flush();
        fos.close();
    }

    public static void writeToFile(File file, String msg) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.write(msg);
        out.close();
    }
//

    public static void writeToFile(String file, byte[] datos) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[6124];
            int bulk;
            InputStream inputStream = new ByteArrayInputStream(datos);
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * getNumRegistros: Cuenta el numero de líneas que existen en un archivo.
     *
     * @param archivo, String ruta del archivo a leer.
     * @return . Long con el numero de lineas existentes en el archivo.
     */
    public static long getNumRegistros(String archivo) {
        Long tamanio = 0L;
        File archivoLeer = new File(archivo);
        try {
            if (archivoLeer.exists()) {
                Scanner iterate = new Scanner(archivoLeer);
                while (iterate.hasNextLine()) {
                    iterate.nextLine();
                    tamanio++;
                }
            }
        } catch (Exception ex) {
            //System.out.println("Excepción Contador " + ex.getMessage() + " en " + archivo);
        }
        return tamanio;
    }

    public static String readFile(String rutaArchivo) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(rutaArchivo);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static File writeToFile(String pathfile, String content) {
        FileOutputStream fos = null;
        File archivoCreado = null;
        try {
            int i;
            fos = new FileOutputStream(pathfile);
            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
            for (i = 0; i < content.length(); i++) {
                out.write(content.charAt(i));
            }
            out.close();
            archivoCreado = new File(pathfile);
        } catch (Exception ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return archivoCreado;
    }

    public static boolean byteToFile(byte[] arrayBytes, String pathFile) {
        boolean respuesta = false;
        try {
            File file = new File(pathFile);
            file.createNewFile();
            FileInputStream fileInputStream = new FileInputStream(pathFile);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayBytes);
            OutputStream outputStream = new FileOutputStream(pathFile);
            int data;
            while ((data = byteArrayInputStream.read()) != -1) {
                outputStream.write(data);
            }
            fileInputStream.close();
            outputStream.close();
            respuesta = true;
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public static void crerArchivo(byte[] datos, String archivo) {
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(datos);
            fos.close();
        } catch (Exception e) {

        }
    }

    public static byte[] getBytes(File f) {
        byte[] bytes = null;
        try {
//            return Files.readAllBytes(Paths.get(f.getAbsolutePath()));
            FileInputStream fis = new FileInputStream(f);
            bytes = getBytes(new FileInputStream(f));
            fis.close();
            fis = null;
        } catch (IOException ex) {
            return null;
        }
        return bytes;
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }
        return buf;
    }

    public static List<File> textoaListaArchivo(String data) {
        List<File> list = new ArrayList(1);
        StringTokenizer st = new StringTokenizer(data, "\r\n");
        while (st.hasMoreTokens()) {
            try {
                list.add(new File(st.nextToken()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

      public static void copyAttrs(File src, File dst) {
        try {
            BasicFileAttributeView attributesSrc = Files.getFileAttributeView(Paths.get(src.getAbsolutePath()), BasicFileAttributeView.class);
            if (attributesSrc != null) {
                BasicFileAttributeView attributesDst = Files.getFileAttributeView(Paths.get(dst.getAbsolutePath()), BasicFileAttributeView.class);
                attributesDst.setTimes(attributesSrc.readAttributes().creationTime(), attributesSrc.readAttributes().lastAccessTime(), attributesSrc.readAttributes().lastModifiedTime());
            }

            FileOwnerAttributeView ownerInfo = Files.getFileAttributeView(src.toPath(), FileOwnerAttributeView.class);
            if (ownerInfo != null) {
                FileOwnerAttributeView ownerInfoDst = Files.getFileAttributeView(dst.toPath(), FileOwnerAttributeView.class);
                ownerInfoDst.setOwner(ownerInfo.getOwner());

            }
            GroupPrincipal group = Files.readAttributes(src.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS).group();
            if (group != null) {
                Files.getFileAttributeView(dst.toPath(), PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).setGroup(group);
            }

            Set<PosixFilePermission> posixPermissions = Files.getPosixFilePermissions(src.toPath());
            if (posixPermissions != null) {
                Files.setPosixFilePermissions(dst.toPath(), posixPermissions);
            }
        } catch (Exception e) {

        }
    }

}

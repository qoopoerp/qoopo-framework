
package net.qoopo.framework.xml;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author alberto
 */
public class XmlUtils {

    public static void escribirArchivoXml(Document documento, String nombreArchivo) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try {
            outputter.output(documento, new FileOutputStream(nombreArchivo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que devuelve el valor contenido en un tag contenido en un String
     * xml.
     *
     * @since 1.0
     * @param tagName
     * @param xml
     * @return
     */
    public static String getValorXml(String tagName, String xml) {
        return getValorXml(tagName, xml, 0);
    }

    /**
     * Método que devuelve valor que se encuentra dentro de tag XML.
     *
     * @since 1.0
     *
     * @param tagNameIni
     * @param tagNameFin
     * @param xml
     * @return
     */
    public static String getValorNameInXml(String tagNameIni, String tagNameFin, String xml) {
        return getValorNameInXml(tagNameIni, tagNameFin, xml, 0);
    }

    /**
     * Método que devuelve el tag y su valor contenido en unString xml.
     *
     * @since 1.0
     * @param tagInicial
     * @param tagFinal
     * @param xml
     * @return
     */
    public static String getValorXmlIncluidoTag(String tagInicial, String tagFinal, String xml) {
        return getValorXmlIncluidoTag(tagInicial, tagFinal, xml, 0);
    }

    public static String getValorXml(String tagName, String xml, int tipo) {
        try {
            String tagIni = "<" + tagName + ">";
            String tagFin = "</" + tagName + ">";
            int in = xml.indexOf(tagIni);
            if (in == -1) {
                return (tipo == 0) ? "" : "0.00";
            }
            int in2 = xml.indexOf(tagFin, in);
            return xml.substring(in + tagIni.length(), in2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (tipo == 0) ? "" : "0.00";
    }

    public static String getValorXmlIncluidoTag(String tagInicial, String tagFinal, String xml, int tipo) {
        try {
            String tagIni = "<" + tagInicial + ">";
            String tagFin = "</" + tagFinal + ">";
            int in = xml.indexOf(tagIni);
            if (in == -1) {
                return (tipo == 0) ? "" : "0.00";
            }
            int in2 = xml.indexOf(tagFin, in);
            return xml.substring(in, in2 + tagFin.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (tipo == 0) ? "" : "0.00";
    }

    public static String getValorNameInXml(String tagNameIni, String tagNameFin, String xml, int tipo) {
        try {
            String tagIni = "<" + tagNameIni + ">";
            String tagFin = "</" + tagNameFin + ">";
            int in = xml.indexOf(tagIni);
            if (in == -1) {
                return (tipo == 0) ? "" : "0.00";
            }
            int in2 = xml.indexOf(tagFin, in);
            return xml.substring(in + tagIni.length(), in2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (tipo == 0) ? "" : "0.00";
    }

    public static void escribirArchivoXml(Document documento, OutputStream stream) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try {
            outputter.output(documento, stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getXmlBytes(Document documento) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = null;
        try {
            outputter.output(documento, bos);
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {

            }
        }
        return data;
    }

}

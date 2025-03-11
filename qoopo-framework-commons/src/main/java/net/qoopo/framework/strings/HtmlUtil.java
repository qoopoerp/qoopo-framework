package net.qoopo.framework.strings;

public class HtmlUtil {

    /**
     * Convierte caracteres con acentos a código html
     */
    public static String prepareHtml(String input) {
        input = input.replace("á", "&aacute;");
        input = input.replace("é", "&eacute;");
        input = input.replace("í", "&iacute;");
        input = input.replace("ó", "&oacute;");
        input = input.replace("ú", "&uacute;");
        input = input.replace("ñ", "&ntilde;");

        input = input.replace("Á", "&Aacute;");
        input = input.replace("É", "&Eacute;");
        input = input.replace("Í", "&Iacute;");
        input = input.replace("Ó", "&Oacute;");
        input = input.replace("Ú", "&Uacute;");
        input = input.replace("Ñ", "&Ntilde;");

        return input;
    }

}

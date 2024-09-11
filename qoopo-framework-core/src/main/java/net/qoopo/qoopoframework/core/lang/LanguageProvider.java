package net.qoopo.qoopoframework.core.lang;

/*
 * Provedor de texto traducido con un codigo de texto que puede ser entero o string y un identificado de lenguage
 */
public interface LanguageProvider {

    public String getTextValue(int textId);

    public String getTextValue(String textId);

    public String getTextValue(int textId, int langId);

    public String getTextValue(String textId, String langId);
}

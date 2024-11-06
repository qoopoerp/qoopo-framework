package net.qoopo.framework.security.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitario que valida si una url corresponde a un patron
 */
public class UrlMatcher {
    private final Pattern pattern;

    public UrlMatcher(String pattern) {
        // Convertimos el patrón a una expresión regular: reemplazamos "*" por ".*"
        String regex = pattern.replace("*", ".*");
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String url) {
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}

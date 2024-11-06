package net.qoopo.framework.security.matcher;

import java.util.regex.Pattern;

import org.apache.commons.math3.exception.NullArgumentException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Valida la solicitud en función de un patrón
 * 
 * Si el patron es "/**" se valida como verdadero a todas las rutas
 */
@RequiredArgsConstructor
public class RegexRequestMatcher extends AbstractRequestMatcher {

    private Pattern pattern;

    private String requestMethod;

    public RegexRequestMatcher(String pattern, String requestMethod) {
        if (pattern == null || pattern.isEmpty()) {
            throw new NullArgumentException();
            // throw NullArgumentException("Patter can not be null");
        }
        //String regex = pattern.replace("*", ".*"); 
        // this.pattern = Pattern.compile(pattern, caseSensitive ? Pattern.DOTALL :
        // Pattern.CASE_INSENSITIVE);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.requestMethod != null && !this.requestMethod.isEmpty() &&
                !requestMethod.equalsIgnoreCase(request.getMethod()))
            return false;

        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();
        if (pathInfo != null || query != null) {
            StringBuilder sb = new StringBuilder(url);
            if (pathInfo != null) {
                sb.append(pathInfo);
            }
            if (query != null) {
                sb.append('?').append(query);
            }
            url = sb.toString();
        }
        return pattern.matcher(url).matches();
    }

}

package net.qoopo.framework.security.matcher;

import net.qoopo.framework.exception.NullArgumentException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Valida la solicitud en función de un patrón
 * 
 * Si el patron es "/**" se valida como verdadero a todas las rutas
 */
@RequiredArgsConstructor
public class UrlRequestMatcher extends AbstractRequestMatcher {

    private UrlMatcher urlMatcher;
    private String requestMethod;

    public UrlRequestMatcher(String pattern) {
        this(pattern, null);
    }

    public UrlRequestMatcher(String pattern, String requestMethod) {
        if (pattern == null || pattern.isEmpty()) {
            throw new NullArgumentException();
            // throw NullArgumentException("Patter can not be null");
        }
        urlMatcher = new UrlMatcher(pattern);
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.requestMethod != null && !this.requestMethod.isEmpty() &&
                !requestMethod.equalsIgnoreCase(request.getMethod()))
            return false;

        String url = getRequestPath(request);
        return urlMatcher.matches(url);
    }

    /**
     * Se ignora el query
     * 
     * @param request
     * @return
     */
    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = (url != null && !url.isEmpty()) ? url + pathInfo : pathInfo;
        }
        return url;
    }

}

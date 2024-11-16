package net.qoopo.framework.security.matcher;

import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Valida si una solicitud tien un Header especificado
 */

public class HttpHeadersMatcher extends AbstractRequestMatcher {

    private static Logger log = Logger.getLogger("HttpHeadersMatcher");

    private String header;
    private String headerValue;

    public HttpHeadersMatcher(String header, String headerValue) {
        this.header = header;
        this.headerValue = headerValue;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String headerRequest = request.getHeader(header);
        return headerRequest != null && headerRequest.contains(headerValue);
    }

}

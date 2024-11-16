package net.qoopo.framework.security.filter.strategy.failure;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Envía un error
 */

public class SendErrorForbiddenStrategy extends SendErrorStrategy {

    public SendErrorForbiddenStrategy() {
        super(HttpServletResponse.SC_FORBIDDEN, "Permiso denegado");
    }

    public SendErrorForbiddenStrategy(String message) {
        super(HttpServletResponse.SC_FORBIDDEN, message);
    }

}

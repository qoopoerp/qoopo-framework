package net.qoopo.framework.security.filter.strategy.failure;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Envía un error
 */

public class SendErrorUnathorizedStrategy extends SendErrorStrategy {

    public SendErrorUnathorizedStrategy() {
        super(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }

    public SendErrorUnathorizedStrategy(String message) {
        super(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

}

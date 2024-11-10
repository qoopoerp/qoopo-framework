package net.qoopo.framework.web.filters;

import java.util.logging.Logger;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounter implements HttpSessionListener {
    private static Logger log = Logger.getLogger("session-counter");
    private static int count;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        count++;
        log.info("session created: " + event.getSession().getId() + " ["
                + event.getSession().getAttribute("user") + "]" + " total=[" + count + "]");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        count--;
        log.info("session destroyed: " + event.getSession().getId() + " ["
                + event.getSession().getAttribute("user") + "] " + " total=[" + count + "]");
    }

    public static int getCount() {
        return count;
    }

}
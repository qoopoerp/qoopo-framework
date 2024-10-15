package net.qoopo.qoopoframework.web.filters;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounter implements HttpSessionListener {

    private static int count;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        count++;
        System.out.println("session created: " + event.getSession().getId() + " ["
                + event.getSession().getAttribute("user") + "]" + " total=[" + count + "]");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        count--;
        System.out.println("session destroyed: " + event.getSession().getId() + " ["
                + event.getSession().getAttribute("user") + "] " + " total=[" + count + "]");
    }

    public static int getCount() {
        return count;
    }

}
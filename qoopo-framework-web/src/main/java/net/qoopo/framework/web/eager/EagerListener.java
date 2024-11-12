package net.qoopo.framework.web.eager;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class EagerListener implements ServletContextListener {

    private static final AnnotationLiteral<Eager> EAGER_ANNOTATION = new AnnotationLiteral<Eager>() {

    };

    @Override
    public void contextInitialized(ServletContextEvent event) {
        for (Object bean : CDI.current().select(EAGER_ANNOTATION)) {
            bean.toString();//los carga perezozamente (este codigo obliga a cargarse al bean)
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // NOOP.
    }

}

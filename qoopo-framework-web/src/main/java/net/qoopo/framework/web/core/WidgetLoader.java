package net.qoopo.framework.web.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.framework.reflection.QoopoReflection;

public class WidgetLoader {

    public static final Logger log = Logger.getLogger("QWidgetLoader");
    private static List<Widget> INSTANCES = new ArrayList<>();

    public static void load() {
        try {
            INSTANCES.clear();
            List<Object> cargados = QoopoReflection.getBeanImplemented(Widget.class);
            cargados.forEach(instance -> {
                Widget widget = (Widget) instance;
                try {
                    log.info("[+] Widget cargado: [".concat(widget.getName()).concat("] ")
                            .concat(widget.getClass().getName()));
                    if (widget.getJsfName() == null) {
                        widget.setJsfName("#{" + widget.getClass().getSimpleName() + "}");
                    }
                    INSTANCES.add(widget);
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void register(Widget widget) {
        if (!INSTANCES.contains(widget)) {
            INSTANCES.add(widget);
        }
    }

    public static List<Widget> getAll() {
        return INSTANCES;
    }
}

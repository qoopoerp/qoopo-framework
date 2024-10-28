package net.qoopo.framework.web.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.Accion;
import net.qoopo.framework.util.ActionWrapper;

/**
 * Clase que representa un widget que puede ser agregado en un tablero
 */
@Getter
@Setter
// net.qoopo.qoopo.web.core.Widget
public abstract class Widget implements Serializable {

    public static final int TYPE_GRAPH = 1;
    public static final int TYPE_TEXT = 2;

    public static final Logger log = Logger.getLogger("Qoopo");

    private String name = "--";
    private String title;
    private String description;
    private String jsfName = null;
    private String jsfFile;
    private int width;
    private int height;
    private boolean maximizable;

    private boolean showProgress;

    private Object value;
    private BigDecimal progress;
    private String footerLabel;
    private String footerValue;
    private int type = TYPE_GRAPH;

    private Accion accion = null;
    private String url = null;
    private List<ActionWrapper> buttons = new ArrayList<>();

    private static List<Widget> INSTANCES = new ArrayList<>();

    /**
     * Ejecuta la accion principal del widget
     */
    public void actionListener() {
        if (accion != null) {
            accion.ejecutar();
        }
    }

    /**
     * ejecuta una accion de los botones configurados en wl widget
     * 
     * @param item
     */
    public void runAction(ActionWrapper item) {
        if (item != null && item.getAction() != null) {
            item.getAction().ejecutar();
        }
    }

    public boolean isShowButtons() {
        return buttons != null && !buttons.isEmpty();
    }

    public boolean isRenderActionListener() {
        return accion != null;
    }

    public boolean isRenderUrl() {
        return url != null;
    }

    public static void load() {
        try {
            INSTANCES.clear();
            ServiceLoader<Widget> cargados = ServiceLoader.load(Widget.class);
            cargados.forEach(widget -> {
                log.info("Widget cargado: [".concat(widget.getName()).concat("] ")
                        .concat(widget.getClass().getName()));
                if (widget.getJsfName() == null) {
                    widget.setJsfName("#{" + widget.getClass().getSimpleName() + "}");
                }
                INSTANCES.add(widget);
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

    public boolean isText() {
        return getType() == TYPE_TEXT;
    }

    public boolean isGraph() {
        return getType() == TYPE_GRAPH;
    }

}

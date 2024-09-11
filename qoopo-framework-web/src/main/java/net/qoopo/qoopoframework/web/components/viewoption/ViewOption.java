package net.qoopo.qoopoframework.web.components.viewoption;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.util.Accion;

@Getter
@Setter
public class ViewOption {

    public static final int LIST = 1;
    public static final int GRID = 2;
    public static final int FORM = 3;
    public static final int GRAPH = 4;
    public static final int CALENDAR = 5;

    public static final int TIMELINE = 6;

    // public static final ViewOption LIST = new ViewOption(TYPE_LIST, null);
    // public static final ViewOption GRID = new ViewOption(TYPE_GRID, null);
    // public static final ViewOption FORM = new ViewOption(TYPE_FORM, null);
    // public static final ViewOption GRAPH = new ViewOption(TYPE_GRAPH, null);
    // public static final ViewOption CALENDAR = new ViewOption(TYPE_CALENDAR,
    // null);

    private boolean enableList;
    private boolean enableForm;
    private boolean enableGrid;
    private boolean enableGraph;
    private boolean enableCalendar;
    private boolean enableTimeline;

    private int value;
    private int defaultValue;
    private Accion accion;

    public ViewOption(Accion accion) {
        this.value = LIST;
        this.defaultValue = this.value;
        this.enableList = true;
        this.enableForm = true;
        this.enableGrid = true;
        this.enableGraph = true;
        this.enableCalendar = true;
        this.enableTimeline = true;
        this.accion = accion;
    }

    public ViewOption(int value, Accion accion) {
        this.value = value;
        this.defaultValue = value;
        this.enableList = true;
        this.enableForm = true;
        this.enableGrid = true;
        this.enableGraph = true;
        this.enableCalendar = true;
        this.enableTimeline = true;
        this.accion = accion;
    }

    public void setValue(int value) {
        this.value = value;
        // valida que siempre sea al menos uno de la opcion y que este permitido
        if ((value != 1 && value != 2 && value != 3 && value != 4 && value != 5 && value != 6)
                || (value == 1 && !enableList)
                || (value == 2 && !enableGrid)
                || (value == 3 && !enableForm)
                || (value == 4 && !enableGraph)
                || (value == 5 && !enableCalendar)
                || (value == 6 && !enableTimeline)) {
            reset();
        }
    }

    public void setUp(int value) {
        setDefaultValue(value);
        setValue(value);
    }

    /**
     * Reset to default value
     */
    public void reset() {
        this.value = this.defaultValue;
    }

    public ViewOption clone() {
        return new ViewOption(this.defaultValue, accion);
    }

    public void execute() {
        if (accion != null) {
            accion.ejecutar();
        }
    }

    public String getCurrentIcon() {
        switch (value) {
            case LIST:
                return "pi-bars";
            case FORM:
                return "pi-tablet";
            case CALENDAR:
                return "pi-calendar";
            case TIMELINE:
                return "pi-align-left";
            case GRAPH:
                return "pi-chart-bar";
            case GRID:
                return "pi-th-large";
            default:
                return "pi-bars";
        }
    }

    public void updateValue(int value) {
        System.out.println("viewoption update value " + value);
        this.value = value;
    }

    public String getStringValue() {
        switch (value) {
            case LIST:
                return "list";
            case FORM:
                return "form";
            case CALENDAR:
                return "calendar";
            case TIMELINE:
                return "timeline";
            case GRAPH:
                return "graph";
            case GRID:
                return "kanban";
            default:
                return "list";
        }
    }
}

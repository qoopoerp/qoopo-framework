package net.qoopo.framework.web.components.daterange;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.date.FechasUtil;

@Getter
@Setter
public class DateRangeController implements Serializable {

    private static Logger log = Logger.getLogger("DateRangeController");

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private MenuModel menuModel;
    private String output;

    private List<RangeItem> itemsDisponibles = new ArrayList<>();

    public DateRangeController() {
        try {
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void build() {
        itemsDisponibles.clear();
        itemsDisponibles.add(range("Hoy", LocalDateTime.now(), LocalDateTime.now()));
        itemsDisponibles.add(range("Ayer", LocalDateTime.now().plusDays(-1), LocalDateTime.now().plusDays(-1)));
        itemsDisponibles.add(
                RangeItemCollection.of("Mes",
                        List.of(fechaEsteMes(),
                                fechaMesAnterior(),
                                monthsOfYear(String.valueOf(LocalDate.now().getYear()), LocalDate.now()),
                                monthsOfYear(String.valueOf(LocalDate.now().plusYears(-1).getYear()),
                                        LocalDate.now().plusYears(-1)),
                                monthsOfYear(String.valueOf(LocalDate.now().plusYears(-2).getYear()),
                                        LocalDate.now().plusYears(-2)),
                                monthsOfYear(String.valueOf(LocalDate.now().plusYears(-3).getYear()),
                                        LocalDate.now().plusYears(-3))

                        )));
        itemsDisponibles.add(
                RangeItemCollection.of("Año",
                        List.of(
                                year(LocalDate.now()),
                                year(LocalDate.now().plusYears(-1)),
                                year(LocalDate.now().plusYears(-2)),
                                year(LocalDate.now().plusYears(-3)))));
        buildMenu();
    }

    public void buildMenu() {
        menuModel = new DefaultMenuModel();
        for (RangeItem item : itemsDisponibles) {
            menuModel.getElements().add(buildMenuItem(item));
        }
        menuModel.generateUniqueIds();
    }

    private MenuElement buildMenuItem(RangeItem item) {
        try {
            if (item instanceof RangeItemCollection) {
                Submenu subMenu = DefaultSubMenu.builder().label(item.getName())
                        .build();
                for (RangeItem child : ((RangeItemCollection) item).getItems())
                    subMenu.getElements().add(buildMenuItem(child));
                return subMenu;
            } else {
                item.setId(new SecureRandom().nextLong());
                return DefaultMenuItem.builder()
                        .value(item.getName())
                        // .icon("pi pi-save")
                        // .ajax(false)
                        .command("#{cc.attrs.value.selectOption(" + item.getId() + ")}")
                        .update("@form")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void selectOption(Long id) {
        try {
            if (itemsDisponibles != null && !itemsDisponibles.isEmpty()) {
                itemsDisponibles.stream().forEach(c -> selectOption(id, c));
            }
        } catch (Exception e) {
            log.severe("Error in select selectOption id");
            e.printStackTrace();
        }
    }

    private void selectOption(Long id, RangeItem item) {
        if (item instanceof RangeItemCollection) {
            ((RangeItemCollection) item).getItems().forEach(c -> selectOption(id, c));
        } else {
            if (item.getId().equals(id)) {
                selectItem(item);
            }
        }
    }

    public void selectItem(RangeItem item) {
        this.startDateTime = item.getStartDateTime();
        this.endDateTime = item.getEndDateTime();
        output = startDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-"
                + endDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private RangeItem range(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return RangeItem.builder().name(name).startDateTime(FechasUtil.fechaInicio(startDateTime))
                .endDateTime(FechasUtil.fechaFin(endDateTime)).build();
    }

    private RangeItem year(LocalDate date) {
        return range(String.valueOf(date.getYear()), FechasUtil.fechaInicioAnio(date),
                FechasUtil.fechaFinAnio(date));
    }

    public RangeItem fechaEsteMes() {
        return range("Mes Actual", FechasUtil.fechaInicioMes(LocalDateTime.now()),
                FechasUtil.fechaFinMes(LocalDateTime.now()));
    }

    public RangeItem fechaMesAnterior() {
        return range("Mes anterior", FechasUtil.fechaInicioMes(LocalDateTime.now().plusMonths(-1)),
                FechasUtil.fechaFinMes(LocalDateTime.now().plusMonths(-1)));
    }

    private RangeItemCollection monthsOfYear(String name, LocalDate date) {
        List<RangeItem> lstMonts = new ArrayList<>();
        LocalDateTime startDate = FechasUtil.fechaInicioAnio(date);
        for (int i = 1; i <= 12; i++) {
            LocalDateTime endDate = FechasUtil
                    .convertToLocalDateTimeViaInstant(FechasUtil.fechaFinMes(FechasUtil.convertLocalDate(startDate)));
            lstMonts.add(range(startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US), startDate, endDate));
            startDate = startDate.plusMonths(1);
        }
        return RangeItemCollection.of(name, lstMonts);
    }
}

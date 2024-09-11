package net.qoopo.qoopoframework.web;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class GestorFecha implements Serializable {

    @Inject
    private AppSessionBeanInterface sessionBean;

    public GestorFecha() {
        // vacio
    }

    public TimeZone getZonaHorariaUTC() {
        return java.util.TimeZone.getTimeZone("GMT");
    }

    public String getZonaHoraria() {
        return sessionBean.getZonaHoraria();
    }

    public Locale getLocale() {
        return java.util.Locale.getDefault();
    }

    public String getTimeZone() {
        return sessionBean.getZonaHoraria();
    }
    // public TimeZone getTimeZone() {
    // return java.util.TimeZone.getTimeZone(Informacion.getZonaHoraria());
    // }

    public ZoneOffset getZoneOffset() {
        return getZoneId().getRules().getOffset(Instant.now());
    }

    public ZoneId getZoneId() {
        return ZoneId.of(sessionBean.getZonaHoraria());
    }

    public List<Locale> getListasLocale() {
        List<Locale> tmp = new ArrayList<>();
        Locale[] lst = Locale.getAvailableLocales();
        if (lst != null && lst.length > 0) {
            tmp.addAll(Arrays.asList(lst));
        }
        return tmp;
    }

    public List<String> getListasTimeZone() {
        List<String> tmp = new ArrayList<>();
        String[] lst = TimeZone.getAvailableIDs();
        if (lst != null && lst.length > 0) {
            tmp.addAll(Arrays.asList(lst));
        }

        Collections.sort(tmp);
        return tmp;
    }

    public String getPatron() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String getPatronSimple() {
        return "dd/MM/yyyy";
    }

    // public static void main(String[] args) {
    //
    // pruebaconvertirFechasZonaHoraria();
    // pruebaconvertirFechasZonaHorariaLocalDateTime();
    //// System.out.println("Locale Default=" + Locale.getDefault());
    //// System.out.println("TimeZone Default=" + TimeZone.getDefault());
    //// System.out.println("");
    //// System.out.println(" LISTA DE LOCALES DISPONIBLES");
    //// System.out.println("");
    ////
    //// Locale[] lst = Locale.getAvailableLocales();
    //// for (Locale lo : lst) {
    //// System.out.println("Locale :" + lo);
    //// System.out.println(" pais=" + lo.getCountry() + " languaje=" +
    // lo.getLanguage() + " display country=" + lo.getDisplayCountry() + " display
    // languaje:" + lo.getDisplayLanguage());
    //// }
    //// System.out.println("");
    //// System.out.println("AHORA VIENEN LOS TIMEZONES");
    //// System.out.println("");
    //// String[] lst2 = TimeZone.getAvailableIDs();
    //// for (String tz : lst2) {
    //// System.out.println(tz);
    //// }
    // }
    //
    // private static void pruebaconvertirFechasZonaHoraria() {
    // //Date will return local time in Java
    // Date localTime = new Date();
    //
    // //creating DateFormat for converting time from local timezone to GMT
    // DateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //
    // //getting GMT timezone, you can get any timezone e.g. UTC
    // converter.setTimeZone(TimeZone.getTimeZone("GMT"));
    //
    // System.out.println("local time : " + localTime);
    // System.out.println("time in GMT : " + converter.format(localTime));
    // }
    //
    // private static void pruebaconvertirFechasZonaHorariaLocalDateTime() {
    // //Date will return local time in Java
    // LocalDateTime localTime = LocalDateTime.now(); //fecha en UTC siempre
    //
    // //creating DateFormat for converting time from local timezone to GMT
    // ZoneId zoneId = ZoneId.of("America/Guayaquil");
    //
    // ZoneOffset zoneOffset = zoneId.getRules().getOffset(Instant.now());
    //
    // DateTimeFormatter converter = DateTimeFormatter.ofPattern("dd/MM/yyyy
    // HH:mm:ss").withZone(zoneId);
    //
    // System.out.println("ZoneId=" + zoneId);
    // System.out.println("offset=" + zoneOffset);
    //
    //// OffsetDateTime offsetDateTime = OffsetDateTime.of(localTime, zoneOffset);
    // OffsetDateTime offsetDateTime = OffsetDateTime.of(localTime, ZoneOffset.UTC);
    //
    // //getting GMT timezone, you can get any timezone e.g. UTC
    // System.out.println("local time : " + converter.format(localTime));
    // System.out.println("offsetDateTime : " + converter.format(offsetDateTime));
    // System.out.println("localTime atZone : " +
    // converter.format(localTime.atZone(ZoneOffset.UTC))); //
    // System.out.println("localTime atZone 2 : " +
    // localTime.atZone(ZoneOffset.UTC).
    // format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a z")
    // .withZone(zoneOffset)));
    // }
}

package net.qoopo.framework.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import net.qoopo.framework.jpa.filter.condicion.Condicion;

@FacesConverter("criterioConverterComplete")
public class CriterioConverterComplete implements Converter<Condicion> {

    public CriterioConverterComplete() {
        // constructor
        // System.out.println("CONVERTER INICIADO");
    }

    @Override
    public Condicion getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            // System.out.println("Converter -- getASObject " + value);
            if (value == null || value.isEmpty()) {
                return null;
            }

            if (Condicion.CONDICIONES != null && !Condicion.CONDICIONES.isEmpty()) {
                for (Condicion condicion : Condicion.CONDICIONES) {
                    if (condicion.hashCode() == Integer.parseInt(value)) {
                        return condicion;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Condicion value) {

        // System.out.println("Converter -- getAsString" + value);
        if (value == null) {
            return null; // Or an empty string, can also.
        }

        if (!(value instanceof Condicion)) {
            throw new ConverterException("El valor no es una Condicion valido: " + value);
        }

        Condicion item = (Condicion) value;
        return String.valueOf(item.hashCode());
    }
}

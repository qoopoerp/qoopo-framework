package net.qoopo.framework.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import net.qoopo.framework.filter.core.condition.Condition;

@FacesConverter("criterioConverterComplete")
public class CriterioConverterComplete implements Converter<Condition> {

    public CriterioConverterComplete() {
        // constructor
        // System.out.println("CONVERTER INICIADO");
    }

    @Override
    public Condition getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            // System.out.println("Converter -- getASObject " + value);
            if (value == null || value.isEmpty()) {
                return null;
            }

            if (Condition.CONDICIONES != null && !Condition.CONDICIONES.isEmpty()) {
                for (Condition condicion : Condition.CONDICIONES) {
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
    public String getAsString(FacesContext context, UIComponent component, Condition value) {

        // System.out.println("Converter -- getAsString" + value);
        if (value == null) {
            return null; // Or an empty string, can also.
        }

        if (!(value instanceof Condition)) {
            throw new ConverterException("El valor no es una Condition valido: " + value);
        }

        Condition item = (Condition) value;
        return String.valueOf(item.hashCode());
    }
}

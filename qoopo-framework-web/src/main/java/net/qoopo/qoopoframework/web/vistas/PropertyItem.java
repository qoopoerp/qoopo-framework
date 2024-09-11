package net.qoopo.qoopoframework.web.vistas;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyItem implements Serializable {

    private String clave;
    private String valor;
}

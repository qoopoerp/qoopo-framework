package net.qoopo.framework.security.core.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Anotación utilizada para indicar que un método o recurso esta asecurado
 * 
 * Actualmente esta implementada las siguientes validaciones
 * 
 * - Servicios rest que tenga la etiqueta se realiza una validación si la
 * solicitud contiene un token válido
 */
@jakarta.ws.rs.NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Secured {

}

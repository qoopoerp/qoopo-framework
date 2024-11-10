package net.qoopo.framework.web;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.context.SecurityContextHolder;

@Named
@SessionScoped
@Getter
@Setter
/**
 * Controlador que se encarga de proveer información sobre el usuario
 * autenticado y la autenticación existente
 */
public class SecurityContextBean implements Serializable {
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}

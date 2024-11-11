package net.qoopo.framework.web;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.qoopo.framework.security.authentication.Authentication;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.context.SecurityContextHolder;
import net.qoopo.framework.util.exceptions.QoopoException;

@Named
@SessionScoped
@Getter
@Setter
/**
 * Controlador que se encarga de proveer información sobre el usuario
 * autenticado y la autenticación existente
 */
public class SecurityContextBean implements Serializable {
    /**
     * Indica si hay un usuario autenticado actualmente
     * 
     * @return
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Devuelve el username del usuario autenticado actualmetne
     * @return
     */
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                Object userValue = authentication.getPrincipal();
                String userName = "";
                if (userValue instanceof String)
                    userName = (String) userValue;
                else if (userValue instanceof UserData)
                    userName = ((UserData) userValue).getUser();

                return userName;
            } catch (Exception e) {

            }
        }
        return null;
    }
}

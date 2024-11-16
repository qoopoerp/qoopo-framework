package net.qoopo.framework.security.filter.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.core.annotation.Secured;
import net.qoopo.framework.security.core.token.TokenProvider;

/**
 * Este filtro se encarga de validar aquellos metodos de los servicios rest que
 * contengan la etiqueta
 * Secured
 * 
 * Solo realiza la validacion que la solicitud tenga un token y que este token
 * sea válido
 * 
 * Una vez que el token sea valido crea una autenticación en el SecurityContext
 */
@Provider
@Secured // indica que solo se aplica a aquellos que tenga la etiqueta Secured
@Priority(Priorities.AUTHENTICATION)
public class SecuredTokenRestFilter implements ContainerRequestFilter {

    private static Logger log = Logger.getLogger("secured-token-filter");
    private List<TokenProvider> tokenProviders;

    // public static final Key KEY = MacProvider.generateKey();
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (SecurityConfig.get().isDebug())
            log.info("[+] Aplicando filtro de SecurityFilter Api Client");

        loadConfig();
        try {
            // Recupera la cabecera HTTP Authorization de la peticion
            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            AtomicBoolean validate = new AtomicBoolean(false);
            tokenProviders.forEach(c -> validate.set(validate.get() || c.validate(authorizationHeader)));
            if (!validate.get()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

            //crea una autenticacion usando el usuario
        } catch (Exception e) {
            e.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void loadConfig() {
        if (tokenProviders == null) {
            tokenProviders = SecurityConfig.get().getTokensProvidersConfigurer().getProviders();
        }
    }
}

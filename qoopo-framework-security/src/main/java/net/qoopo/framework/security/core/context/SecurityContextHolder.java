package net.qoopo.framework.security.core.context;

import java.util.logging.Logger;

import net.qoopo.framework.security.config.SecurityConfig;
import net.qoopo.framework.security.core.context.strategy.GlobalSecurityContextHolderStrategy;
import net.qoopo.framework.security.core.context.strategy.InheritableThreadLocalSecurityContextHolderStrategy;
import net.qoopo.framework.security.core.context.strategy.SecurityContextHolderStrategy;
import net.qoopo.framework.security.core.context.strategy.ThreadLocalSecurityContextHolderStrategy;

/**
 * Almacena y administra el SecurityContext según la estrategia definida
 */
public class SecurityContextHolder {

    private static Logger log = Logger.getLogger("SecurityContextHolder");
    public static final String MODE_GLOBAL = "GLOBAL";
    public static final String THREAD_LOCAL = "THREAD_LOCAL";
    public static final String INHERITABLE_THREAD_LOCAL = "INHERITABLE_THREAD_LOCAL";
    public static final String MODE_USER_DEFINED = "USER_DEFINED";

    private static final String ENV_VARIABLE = "qoopo_framework_security_strategy";

    private static SecurityContextHolderStrategy strategy;

    private static String strategyName;

    private static int initializeCount = 0;

    static {
        init();
    }

    private static void init() {
        initStrategy();
        initializeCount++;
    }

    private static void initStrategy() {
        try {
            if (strategyName != null && strategyName.equals(MODE_USER_DEFINED)) {
                return;
            }

            strategyName = System.getenv(ENV_VARIABLE);
            if (strategyName == null) {
                strategyName = THREAD_LOCAL;
            }
            if (SecurityConfig.get().isDebug())
                log.info("[+] SecurityContextHolderStrategy -> " + strategyName);

            switch (strategyName) {
                case MODE_GLOBAL:
                    strategy = new GlobalSecurityContextHolderStrategy();
                    break;
                case THREAD_LOCAL:
                    strategy = new ThreadLocalSecurityContextHolderStrategy();
                    break;
                case INHERITABLE_THREAD_LOCAL:
                    strategy = new InheritableThreadLocalSecurityContextHolderStrategy();
                    break;
                default:
                    strategy = new InheritableThreadLocalSecurityContextHolderStrategy();
                    break;
            }
        } catch (Exception e) {

        }
    }

    public static void setContext(SecurityContext context) {
        if (SecurityConfig.get().isDebug())
            log.info("[+] Seteando securityContext, instancias:" + initializeCount);
        strategy.setContext(context);
    }

    public static SecurityContext getContext() {
        return strategy.getContext();
    }

    public static void clear() {
        if (SecurityConfig.get().isDebug())
            log.info("[+] Limpiando securityContext, instancias:" + initializeCount);
        strategy.clear();
    }

    public static SecurityContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    public static int getInitializeCount() {
        return initializeCount;
    }

    public static void setStrategyName(String strategyName) {
        SecurityContextHolder.strategyName = strategyName;
        init();
    }

    public static void setStrategy(SecurityContextHolderStrategy strategy) {
        SecurityContextHolder.strategyName = MODE_USER_DEFINED;
        SecurityContextHolder.strategy = strategy;
        init();
    }

}

package net.qoopo.framework.security.context;

/**
 * Almacena y administra el SecurityContext según la estrategia definida
 */
public class SecurityContextHolder {

    public static final String MODE_GLOBAL = "GLOBAL";
    public static final String MODE_USER_DEFINED = "USER_DEFINED";

    private static final String ENV_VARIABLE = "qoopo.framework.security.strategy";

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
                strategyName = MODE_GLOBAL;
            }

            switch (strategyName) {
                case MODE_GLOBAL:
                    strategy = new GlobalSecurityContextHolderStrategy();
                    break;
                default:
                    strategy = new GlobalSecurityContextHolderStrategy();
                    break;
            }
        } catch (Exception e) {

        }
    }

    public static void setContext(SecurityContext context) {
        strategy.setContext(context);
    }

    public static SecurityContext getContext() {
        return strategy.getContext();
    }

    public static void clear() {
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

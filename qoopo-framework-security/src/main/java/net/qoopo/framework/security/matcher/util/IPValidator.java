package net.qoopo.framework.security.matcher.util;

import java.util.regex.Pattern;

public class IPValidator {

    private static final String IPV4_PATTERN = "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\." +
            "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\." +
            "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\." +
            "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})$";

    private static final Pattern patternIpV4 = Pattern.compile(IPV4_PATTERN);

    private static final String IPV6_PATTERN = "([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})";

    private static final Pattern patternIpV6 = Pattern.compile(IPV6_PATTERN);

    public static boolean isValidIPv4(String ip) {
        return patternIpV4.matcher(ip).matches();
    }

    public static boolean isValidIPv6(String ip) {
        return patternIpV6.matcher(ip).matches();
    }

    public static boolean isValidIP(String ip) {
        return isValidIPv4(ip) || isValidIPv6(ip);
    }
}

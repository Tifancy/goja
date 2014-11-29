/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-11-22 15:02
 * @since JDK 1.6
 */
public class EmbededConfig {

    public static final String CONFIG_PORT    = "server.port";
    public static final String CONFIG_SSL     = "server.ssl";
    public static final String CONFIG_WORDERS = "server.undertow.workers";
    public static final String CONFIG_IOS     = "server.undertow.ios";

    public static int getConfigPort() {
        return GojaConfig.getPropertyToInt(CONFIG_PORT, 9000);
    }

    public static boolean getConfigSsl() {
        return GojaConfig.getPropertyToBoolean(CONFIG_SSL, false);
    }

    private EmbededConfig() {
    }

    public static int getConfigWorders() {
        return GojaConfig.getPropertyToInt(CONFIG_WORDERS, 0);
    }

    public static int getConfigIos() {
        return GojaConfig.getPropertyToInt(CONFIG_IOS, 0);
    }
}

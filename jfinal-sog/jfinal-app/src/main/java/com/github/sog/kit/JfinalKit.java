/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.kit;

import com.github.sog.kit.common.Reflect;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.IPlugin;

import java.util.List;

public class JfinalKit {


    static {
        init();
    }

    private static List<IPlugin> pluginList;
    private static Constants     constants;
    private static Routes        routes;
    private static Plugins       plugins;
    private static Interceptors  interceptors;
    private static Handlers      handlers;

    public static void init() {
        Reflect reflect = Reflect.on("com.jfinal.core.Config");
        constants = reflect.get("constants");
        routes = reflect.get("routes");
        plugins = reflect.get("plugins");
        interceptors = reflect.get("interceptors");
        handlers = reflect.get("handlers");
        pluginList = plugins.getPluginList();
    }

    public static Constants getConstants() {
        return JfinalKit.constants;
    }

    public static Routes getRoutes() {
        return JfinalKit.routes;
    }

    public static Plugins getPlugins() {
        return JfinalKit.plugins;
    }

    public static Interceptors getInterceptors() {
        return JfinalKit.interceptors;
    }

    public static Handlers getHandlers() {
        return JfinalKit.handlers;
    }

    public static void stopPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
            }
        }
    }

    public static void startPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.start();
            }
        }
    }

    public static void restartPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
                iPlugin.start();
            }
        }
    }

}

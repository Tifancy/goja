/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.plugin.config;

import com.google.common.collect.Lists;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

import java.util.List;

public class ConfigPlugin implements IPlugin {

    static String suffix = "txt";

    protected final Logger logger = Logger.getLogger(getClass());

    private final List<String> includeResources = Lists.newArrayList();

    private final List<String> excludeResources = Lists.newArrayList();

    private boolean reload = true;

    public ConfigPlugin(String... includeResources) {
        if (includeResources != null) {
            for (String includeResource : includeResources) {
                this.includeResources.add(includeResource);
            }
        }
    }

    public static void setSuffix(String suffix) {
        ConfigPlugin.suffix = suffix;
    }

    public ConfigPlugin excludeResource(String... resource) {
        if (includeResources != null) {
            for (String excludeResource : excludeResources) {
                excludeResources.add(excludeResource);
            }
        }
        return this;
    }

    public ConfigPlugin addResource(String resource) {
        includeResources.add(resource);
        return this;
    }

    public ConfigPlugin reload(boolean reload) {
        this.reload = reload;
        return this;
    }

    @Override
    public boolean start() {
        ConfigKit.init(includeResources, excludeResources, reload);
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

}

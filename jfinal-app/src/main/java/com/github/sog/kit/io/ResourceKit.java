/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.kit.io;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.github.sog.config.StringPool;

public class ResourceKit {
    public static Map<String, String> readProperties(String resourceName) {
        Properties properties = new Properties();
        URL resource = Resources.getResource(resourceName);
        try {
            properties.load(new InputStreamReader(resource.openStream(), StringPool.UTF_8));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        return Maps.fromProperties(properties);
    }

}

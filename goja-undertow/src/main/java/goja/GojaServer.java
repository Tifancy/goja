/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja;

import ch.qos.logback.classic.selector.servlet.ContextDetachingSCL;
import com.jfinal.core.JFinalFilter;
import goja.init.InitConst;
import goja.init.ctxbox.ClassFinder;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;

import javax.servlet.ServletException;
import java.util.Properties;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-11-22 14:37
 * @since JDK 1.6
 */
public class GojaServer {

    private final int port;

    private static DeploymentManager manager;
    private static Undertow          server;

    public GojaServer() {
        this.port = EmbededConfig.getConfigPort();
    }

    public synchronized void start() {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setDeploymentName("goja#Server")
                .setClassLoader(GojaServer.class.getClassLoader());

        final Properties p = GojaConfig.getConfigProps();
        String app_name = GojaConfig.appName();
        servletBuilder.setContextPath(StringPool.SLASH + app_name);


        if (GojaConfig.enable_security()) {
            servletBuilder.addListeners(Servlets.listener(EnvironmentLoaderListener.class));
            servletBuilder.addFilter(Servlets.filter("Goja#ShiroFilter", ShiroFilter.class));
        }
        // init logger
        goja.Logger.init();
        //logger context destroy listener.
        servletBuilder.addListeners(Servlets.listener(ContextDetachingSCL.class));
        //Before starting JFinal, lookup class file on the classpath.
        ClassFinder.find();


        servletBuilder.addFilters(
                Servlets.filter("Goja#Jfinal!", JFinalFilter.class)
                        .addInitParam("configClass", "goja.Goja")
                        .setAsyncSupported(true)
        );

        manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = null;
        try {
            path = Handlers.path(Handlers.redirect(app_name))
                    .addPrefixPath(StringPool.SLASH + app_name, manager.start());
        } catch (ServletException e) {
            e.printStackTrace();
        }

        server = Undertow.builder()
                .addHttpListener(this.port, "localhost")
                .setHandler(path)
                .build();
        server.start();
    }

    public synchronized void stop() {
        if (manager != null) {
            manager.undeploy();
        }
        if (server != null) {
            server.stop();
        }
    }
}

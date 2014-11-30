/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package goja.test;

import com.alibaba.druid.util.JdbcUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;
import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.kit.PathKit;
import goja.Goja;
import goja.GojaConfig;
import goja.StringPool;
import goja.init.ctxbox.ClassFinder;
import goja.kits.reflect.Reflect;
import goja.test.mock.MockHttpServletRequest;
import goja.test.mock.MockHttpServletResponse;
import goja.test.mock.MockServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.junit.AfterClass;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static goja.StringPool.*;

public abstract class ControllerTestCase {
    protected static final Logger logger = LoggerFactory.getLogger(ControllerTestCase.class);

    protected static ServletContext servletContext = new MockServletContext();


    protected static MockHttpServletRequest  request;
    protected static MockHttpServletResponse response;
    protected static Handler                 handler;

    private static boolean configStarted = false;

    private static JFinalConfig configInstance;

    private String actionUrl;
    private String bodyData;

    private File bodyFile;
    private File responseFile;

    private final Class<? extends JFinalConfig> config = Goja.class;


    private static void initConfig(JFinal me, ServletContext servletContext, JFinalConfig config) {
        Reflect.on(me).call("init", config, servletContext);
    }

    public static void start(Class<? extends JFinalConfig> configClass) throws Exception {
        if (configStarted) {
            return;
        }
        final Properties configProps = GojaConfig.getConfigProps();
        runScriptInitDb(configProps);
        //Before starting JFinal, lookup class file on the classpath.
        ClassFinder.findWithTest();

        JFinal me = JFinal.me();
        configInstance = configClass.newInstance();
        initConfig(me, servletContext, configInstance);
        handler = Reflect.on(me).get("handler");
        configStarted = true;
        configInstance.afterJFinalStart();

    }

    private static void runScriptInitDb(final Properties p) {
        try {

            String script_path = p.getProperty("db.script.path", "misc/sql/");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(script_path), "The Database init database script init!");
            final String real_script_path = PathKit.getRootClassPath() + File.separator + script_path;
            if (logger.isDebugEnabled()) {
                logger.debug("init db script with {}", real_script_path);
            }
            final File script_dir = new File(real_script_path);
            if (script_dir.exists() && script_dir.isDirectory()) {
                final String db_url = GojaConfig.dbUrl();
                Preconditions.checkNotNull(db_url, "The DataBase connection url is must!");
                Collection<File> list_script_files
                        = Ordering.natural()
                        .sortedCopy(FileUtils.listFiles(script_dir, new String[]{"sql"}, false));
                for (File list_script_file : list_script_files) {
                    final SQLExec sql_exec = new SQLExec();
                    final String driverClassName = JdbcUtils.getDriverClassName(db_url);
                    sql_exec.setDriver(driverClassName);
                    sql_exec.setUrl(db_url);
                    final String db_username = MoreObjects.firstNonNull(GojaConfig.dbUsername(), "root");
                    final String db_password = MoreObjects.firstNonNull(GojaConfig.dbPwd(), "123456");
                    sql_exec.setUserid(db_username);
                    sql_exec.setPassword(db_password);

                    sql_exec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));
                    sql_exec.setPrint(true);
                    sql_exec.setProject(new Project());
                    sql_exec.setSrc(list_script_file);
                    sql_exec.execute();
                }

            }
        } catch (SQLException e) {
            logger.error("init db script is error!", e);
            throw Throwables.propagate(e);
        }
    }

    @AfterClass
    public static void stop() throws Exception {
        configInstance.beforeJFinalStop();
    }

    public Object findAttrAfterInvoke(String key) {
        return request.getAttribute(key);
    }

    private String getTarget(String url, MockHttpServletRequest request) {
        String target = url;
        if (url.contains(QUESTION_MARK)) {
            target = url.substring(0, url.indexOf(QUESTION_MARK));
            String queryString = url.substring(url.indexOf(QUESTION_MARK) + 1);
            String[] keyVals = queryString.split(AMPERSAND);
            for (String keyVal : keyVals) {
                int i = keyVal.indexOf('=');
                String key = keyVal.substring(0, i);
                String val = keyVal.substring(i + 1);
                request.setParameter(key, val);
            }
        }
        return target;

    }

    @Before
    public void init() throws Exception {
        start(config);
    }

    public String invoke() {
        if (bodyFile != null) {
            List<String> req = null;
            try {
                req = Files.readLines(bodyFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
            if (req != null) {
                bodyData = Joiner.on(EMPTY).join(req);
            }
        }
        StringWriter resp = new StringWriter();
        request = new MockHttpServletRequest(new MockServletContext());
        response = new MockHttpServletResponse();
        Reflect.on(handler).call("handle", getTarget(actionUrl, request), request, response, new boolean[]{true});
        String response = resp.toString();
        if (responseFile != null) {
            try {
                Files.write(response, responseFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return response;
    }


    public ControllerTestCase post(File bodyFile) {
        this.bodyFile = bodyFile;
        return this;
    }

    public ControllerTestCase post(String bodyData) {
        this.bodyData = bodyData;
        return this;
    }

    public ControllerTestCase use(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public ControllerTestCase writeTo(File responseFile) {
        this.responseFile = responseFile;
        return this;
    }

}

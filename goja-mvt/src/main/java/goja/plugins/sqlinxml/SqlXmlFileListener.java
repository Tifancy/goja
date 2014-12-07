/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.plugins.sqlinxml;

import com.google.common.io.Files;
import goja.kits.JaxbKit;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static goja.StringPool.DOT;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-06 23:45
 * @since JDK 1.6
 */
public class SqlXmlFileListener extends FileAlterationListenerAdaptor {

    public static final SqlXmlFileListener me = new SqlXmlFileListener();

    private static final Logger logger = LoggerFactory.getLogger(SqlXmlFileListener.class);

    public SqlXmlFileListener() {
    }

    @Override
    public void onDirectoryCreate(File directory) {
        logger.info("the directory {} create!", directory);
        SqlKit.reload();
    }

    @Override
    public void onDirectoryDelete(File directory) {
        logger.info("the directory {} delete!", directory);
        SqlKit.reload();
    }

    @Override
    public void onDirectoryChange(File directory) {
        logger.info("the directory {} change!", directory);
        SqlKit.reload();
    }

    private void reload(File change_file) {
        SqlGroup group;
        if (change_file.isFile() && Files.getFileExtension(change_file.getAbsolutePath()).endsWith(SqlKit.CONFIG_SUFFIX)) {
            group = JaxbKit.unmarshal(change_file, SqlGroup.class);
            String name = group.name;
            if (StringUtils.isBlank(name)) {
                name = change_file.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                SqlKit.putOver(name + DOT + sqlItem.id, sqlItem.value);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("reload file:" + change_file.getAbsolutePath());
            }
        }
    }

    private void removeFile(File remove_file) {
        SqlGroup group;
        if (remove_file.isFile() && Files.getFileExtension(remove_file.getAbsolutePath()).endsWith(SqlKit.CONFIG_SUFFIX)) {
            group = JaxbKit.unmarshal(remove_file, SqlGroup.class);
            String name = group.name;
            if (StringUtils.isBlank(name)) {
                name = remove_file.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                SqlKit.remove(name + DOT + sqlItem.id);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("delete file:" + remove_file.getAbsolutePath());
            }
        }
    }


    @Override
    public void onFileCreate(File file) {
        logger.info("the file {} create!", file);
        reload(file);
    }

    @Override
    public void onFileChange(File file) {
        logger.info("the file {} change!", file);
        reload(file);
    }

    @Override
    public void onFileDelete(File file) {
        logger.info("the file {} delete!", file);
        removeFile(file);
    }
}

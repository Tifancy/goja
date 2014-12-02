/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static goja.StringPool.EMPTY;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-12-02 09:44
 * @since JDK 1.6
 */
public class QDsl {
    private static final Logger logger = LoggerFactory.getLogger(QDsl.class);

    private static final StringBuilder _SQL_BUILDER = new StringBuilder(EMPTY);

    /**
     * 获取单例对象,如果要调用该单例的使用,只能通过该方法获取.
     */
    public static QDsl me() {
        _SQL_BUILDER.setLength(IntPool.ZERO);
        return QDslHolder.instance;
    }

    /**
     * 私有构造函数,确保对象只能通过单例方法来调用.
     */
    private QDsl() { }


    public <M extends Model> QDsl from(M model) {

        return this;
    }

    public QDsl where() {
        return this;
    }

    public QDsl orderBy() {
        return this;
    }

    public QDsl groupBy() {
        return this;
    }


    public <M extends Model> List<M> list() {
        return Lists.newArrayList();
    }

    public <M extends Model> M one() {
        return null;
    }

    /**
     * lazy 加载的内部类,用于实例化单例对象.
     */
    private static class QDslHolder {
        static QDsl instance = new QDsl();
    }
}

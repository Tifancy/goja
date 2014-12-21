/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.db;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import goja.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-06-02 11:46
 * @since JDK 1.6
 */
public class DaoKit {
    public static final String SQL_PIRFIX_WHERE   = ".where";
    public static final String SQL_PIRFIX_COLUMNS = ".column";
    public static final String SQL_PIRFIX_ORDERS  = ".order";


    /**
     * 根据默认主键<code>id</code>和实体判断是否为新构成的实体。
     *
     * @param m   实体Model
     * @param <M> 泛型实体参数
     * @return 如果为新构成的则返回true
     */
    public static <M extends Model> boolean isNew(M m) {
        return isNew(m, StringPool.PK_COLUMN);
    }

    /**
     * Query the database record set.
     *
     * @param sqlSelect Sql Select
     * @return query result.
     * @see SqlSelect
     * @see FindBy
     */
    public static List<Record> findBy(SqlSelect sqlSelect) {
        Preconditions.checkNotNull(sqlSelect, "The Query Sql is must be not null.");
        return Db.find(sqlSelect.toString(), sqlSelect.getParams().toArray());
    }

    /**
     * Query a data record.
     *
     * @param sqlSelect Sql Select
     * @return query result.
     * @see SqlSelect
     * @see FindBy
     */
    public static Record findOne(SqlSelect sqlSelect) {
        Preconditions.checkNotNull(sqlSelect, "The Query Sql is must be not null.");
        return Db.findFirst(sqlSelect.toString(), sqlSelect.getParams().toArray());
    }


    /**
     * 根据主键和实体判断是否为新构成的实体。
     *
     * @param m         实体Model
     * @param pk_column 主键列
     * @param <M>       泛型实体参数
     * @return 如果为新构成的则返回true
     */
    public static <M extends Model> boolean isNew(M m, String pk_column) {
        final Number number = m.getNumber(pk_column);
        return number == null || number.intValue() <= 0;
    }

    /**
     * 拼接LIKE SQL 百分号
     *
     * @param value 数值
     * @return SQL LIKE表达形式
     */
    public static String like(String value) {
        return StringPool.PERCENT + Strings.nullToEmpty(value) + StringPool.PERCENT;
    }




}

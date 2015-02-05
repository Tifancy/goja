/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.rapid.db;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import goja.StringPool;
import goja.plugins.sqlinxml.SqlKit;
import goja.rapid.datatables.DTCriterias;
import goja.rapid.datatables.DTOrder;
import goja.rapid.page.PageDto;
import org.apache.commons.lang3.StringUtils;

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
     * According to the default primary key <code>id</code> is for the new data and entity.
     *
     * @param m   model
     * @param <M> Generic entity references
     * @return If for the new form of return true.
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
     * According to the primary key and entity determine whether for the new entity.
     *
     * @param m         Database model.
     * @param pk_column PK Column.
     * @param <M>       Generic entity references.
     * @return If for the new form of return true.
     */
    public static <M extends Model> boolean isNew(M m, String pk_column) {
        final Number number = m.getNumber(pk_column);
        return number == null || number.intValue() <= 0;
    }

    /**
     * Stitching LIKE SQL percent.
     *
     * @param value value
     * @return SQL LIKE expression.
     */
    public static String like(String value) {
        return StringPool.PERCENT + Strings.nullToEmpty(value) + StringPool.PERCENT;
    }


    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param model_name sql-conf sqlgroup name.
     * @param criterias  required parameter
     * @return Paging data.
     */
    public static Page<Record> paginate(String model_name, DTCriterias criterias) {
        return paginate(model_name, criterias, null);
    }

    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param model_name sql-conf sqlgroup name.
     * @param criterias  required parameter
     * @return Paging data.
     */
    public static Page<Record> paginate(String model_name, DTCriterias criterias, List<Object> params) {
        return paginate(SqlKit.sql(model_name + SQL_PIRFIX_WHERE)
                , SqlKit.sql(model_name + SQL_PIRFIX_COLUMNS)
                , criterias, SqlKit.sql(model_name + SQL_PIRFIX_ORDERS), params);
    }

    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param where         FROM WHERE SQL.
     * @param sql_columns   SELECT column sql.
     * @param criterias     required parameter
     * @param default_order The default sort field, similar: ORDER BY DESC id.
     * @return Paging data.
     */
    public static Page<Record> paginate(String where
            , String sql_columns
            , DTCriterias criterias
            , String default_order
            , List<Object> params) {
        int pageSize = criterias.getLength();
        int start = criterias.getStart() / pageSize + 1;
        final List<DTOrder> order = criterias.getOrder();
        if (order != null && !order.isEmpty()) {
            StringBuilder orderBy = new StringBuilder();
            for (DTOrder _order : order)
                orderBy.append(_order.getColumn()).append(StringPool.SPACE).append(_order.getDir());
            final String byColumns = orderBy.toString();
            if (!Strings.isNullOrEmpty(byColumns)) {
                where += " ORDER BY " + byColumns;
            }
        }
        if (!where.contains("ORDER")) {
            where += (StringPool.SPACE + default_order);
        }
        if (params == null || params.size() == 0) {
            return Db.paginate(start, pageSize, sql_columns, where);
        } else {

            return Db.paginate(start, pageSize, sql_columns, where, params.toArray());
        }
    }


    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param where         FROM WHERE SQL.
     * @param sql_columns   SELECT column sql.
     * @param pageDto       required parameter.
     * @param default_order he default sort field, similar: ORDER BY DESC id.
     * @return Paging data.
     */
    public static Page<Record> paginate(String where, String sql_columns, String default_order, PageDto pageDto) {
        where = Strings.nullToEmpty(where);
        int pageSize = pageDto.pageSize;
        int p = pageDto.page;
        int start = ((p - 1) * pageSize) + 1;
        final List<PageDto.ReqParam> params = pageDto.params;
        final List<Object> query_params = pageDto.query_params;
        if ((params == null || params.isEmpty()) && (query_params == null || query_params.isEmpty())) {
            return Db.paginate(start, pageSize, sql_columns, where);
        } else {
            if (!StringUtils.containsIgnoreCase(where, "WHERE")) {
                where = where + " WHERE 1=1 ";
            }
            for (PageDto.ReqParam param : pageDto.params) {
                where += param.toSql();
            }

            return Db.paginate(start, pageSize, sql_columns, where + default_order, query_params.toArray());
        }
    }

}

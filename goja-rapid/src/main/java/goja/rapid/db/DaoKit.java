/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.rapid.db;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import goja.StringPool;
import goja.castor.Castors;
import goja.lang.Lang;
import goja.plugins.sqlinxml.SqlKit;
import goja.rapid.datatables.DTCriterias;
import goja.rapid.datatables.DTOrder;
import goja.rapid.page.PageDto;
import goja.tuples.Triplet;
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

    /**
     * Gets the data values by specifying the data type, and automatically convert types
     *
     * @param m    Model
     * @param attr database table field.
     * @param cls  data type.
     * @param <T>  specifying the data type.
     * @param <M>  model type.
     * @return data value.
     */
    public static <T, M extends Model> T getData(M m, String attr, Class<T> cls) {
        Object value = m.get(attr);
        return Castors.me().castTo(value, cls);
    }


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
     * Stitching LEFT LIKE SQL percent.
     *
     * @param value value
     * @return SQL LIKE expression.
     */
    public static String llike(String value) {
        return StringPool.PERCENT + Strings.nullToEmpty(value);
    }

    /**
     * Stitching Right LIKE SQL percent.
     *
     * @param value value
     * @return SQL LIKE expression.
     */
    public static String rlike(String value) {
        return Strings.nullToEmpty(value) + StringPool.PERCENT;
    }


    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param model_name sql-conf sqlgroup name.
     * @param criterias  required parameter
     * @return Paging data.
     */
    public static Page<Record> paginate(String model_name,
                                        DTCriterias criterias) {
        return paginate(model_name, criterias, Lists.newArrayListWithCapacity(1));
    }

    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param model_name sql-conf sqlgroup name.
     * @param criterias  required parameter
     * @return Paging data.
     */
    public static Page<Record> paginate(String model_name,
                                        DTCriterias criterias,
                                        List<Object> params) {
        return paginate(SqlKit.sql(model_name + SQL_PIRFIX_WHERE)
                , SqlKit.sql(model_name + SQL_PIRFIX_COLUMNS)
                , criterias, params);
    }

    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param where       FROM WHERE SQL.
     * @param sql_columns SELECT column sql.
     * @param criterias   required parameter
     * @return Paging data.
     */
    public static Page<Record> paginate(String where,
                                        String sql_columns,
                                        DTCriterias criterias,
                                        List<Object> params) {
        int pageSize = criterias.getLength();
        int start = criterias.getStart() / pageSize + 1;

        StringBuilder where_sql = new StringBuilder(where);

        final List<Triplet<String, Condition, Object>> custom_params = criterias.getParams();
        if (!custom_params.isEmpty()) {
            boolean append_and = StringUtils.containsIgnoreCase(where, "WHERE");
            if (!append_and) {
                where_sql.append(" WHERE ");
            }
            for (Triplet<String, Condition, Object> custom_param : custom_params) {
                if (append_and) {
                    where_sql.append(" AND ");
                }
                where_sql.append(custom_param.getValue0());
                final Condition con = custom_param.getValue1();
                where_sql.append(con.condition);
                switch (con) {
                    case BETWEEN:
                        final Object[] value2 = (Object[]) custom_param.getValue2();
                        params.add(value2[0]);
                        params.add(value2[1]);
                        break;
                    default:
                        params.add(custom_param.getValue2());
                        break;
                }
                append_and = true;
            }
        }

        final List<DTOrder> order = criterias.getOrder();
        if (!Lang.isEmpty(order)) {
            StringBuilder orderBy = new StringBuilder();
            for (DTOrder _order : order)
                orderBy.append(_order.getColumn()).append(StringPool.SPACE).append(_order.getDir());
            final String byColumns = orderBy.toString();
            if (!Strings.isNullOrEmpty(byColumns)) {
                where_sql.append(" ORDER BY ").append(byColumns);
            }
        }

        if (Lang.isEmpty(params)) {
            return Db.paginate(start, pageSize, sql_columns, where_sql.toString());
        } else {

            return Db.paginate(start, pageSize, sql_columns, where_sql.toString(), params.toArray());
        }
    }


    /**
     * Paging retrieve, default sorted by id, you need to specify the datatables request parameters.
     *
     * @param where         FROM WHERE SQL.
     * @param sql_columns   SELECT column sql.
     * @param pageDto       required parameter.
     * @return Paging data.
     */
    public static Page<Record> paginate(String where,
                                        String sql_columns,
                                        PageDto pageDto) {
        where = Strings.nullToEmpty(where);
        int pageSize = pageDto.pageSize;
        int p = pageDto.page;
        int start = ((p - 1) * pageSize) + 1;
        final List<RequestParam> params = pageDto.params;
        final List<Object> query_params = pageDto.query_params;
        if ((params == null || params.isEmpty()) && (query_params == null || query_params.isEmpty())) {
            return Db.paginate(start, pageSize, sql_columns, where);
        } else {
            if (!StringUtils.containsIgnoreCase(where, "WHERE")) {
                where = where + " WHERE 1=1 ";
            }
            for (RequestParam param : pageDto.params) {
                where += param.toSql();
            }

            return Db.paginate(start, pageSize, sql_columns, where, query_params.toArray());
        }
    }

}

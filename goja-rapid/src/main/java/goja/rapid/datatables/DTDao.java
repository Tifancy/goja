package goja.rapid.datatables;

import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import goja.StringPool;
import goja.db.DaoKit;
import goja.lang.Lang;
import goja.plugins.sqlinxml.SqlKit;
import goja.rapid.datatables.core.DTColumn;
import goja.rapid.datatables.core.DTCriterias;
import goja.rapid.datatables.core.DTOrder;

import java.util.List;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class DTDao {


    /**
     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
     *
     * @param model_name sql conf 中的 sqlGroup 的name
     * @param criterias  请求参数
     * @return 分页数据
     */
    public static Page<Record> paginate(String model_name, DTCriterias criterias) {
        return paginate(model_name, criterias, null);
    }

    /**
     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
     *
     * @param model_name sql conf 中的 sqlGroup 的name
     * @param criterias  请求参数
     * @return 分页数据
     */
    public static Page<Record> paginate(String model_name, DTCriterias criterias, List<Object> params) {
        return paginate(SqlKit.sql(model_name + DaoKit.SQL_PIRFIX_WHERE)
                , SqlKit.sql(model_name + DaoKit.SQL_PIRFIX_COLUMNS)
                , criterias, SqlKit.sql(model_name + DaoKit.SQL_PIRFIX_ORDERS), params);
    }

    /**
     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
     *
     * @param where         FROM WHERE 语句.
     * @param sql_columns   SELECT column sql 语句
     * @param criterias     请求参数
     * @param default_order 默认的排序字段，类似：ORDER BY id DESC
     * @return 分页数据
     */
    public static Page<Record> paginate(String where, String sql_columns, DTCriterias criterias, String default_order, List<Object> params) {
        int pageSize = criterias.getLength();
        int start = criterias.getStart() ;
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
     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
     *
     * @param model     实体
     * @param criterias 请求参数
     * @return 分页数据
     */
    public static Page<Record> paginate(Class<? extends Model> model, DTCriterias criterias, List<Object> params) {
        int pageSize = criterias.getLength();
        int start = criterias.getStart();

        final Table table = TableMapping.me().getTable(model);
        final String tableName = table.getName();
        String where = " FROM " + tableName + StringPool.SPACE;

        final List<DTColumn> columns = criterias.getColumns();

        String sql_columns = null;
        if (!Lang.isEmpty(columns)) {
            StringBuilder sql_builder = new StringBuilder("SELECT ");
            boolean first = false;
            for (DTColumn column : columns) {
                if (column != null) {
                    if (first) {
                        sql_builder.append(StringPool.COMMA).append(StringPool.SPACE).append(column.getData());
                    } else {
                        sql_builder.append(column.getData());
                        first = true;
                    }
                }
            }
            sql_columns = sql_builder.toString();
        }

        final List<DTOrder> order = criterias.getOrder();
        if (!Lang.isEmpty(order)) {
            StringBuilder orderBy = new StringBuilder();
            for (DTOrder _order : order)
                orderBy.append(_order.getColumn()).append(StringPool.SPACE).append(_order.getDir());
            final String byColumns = orderBy.toString();
            if (!Strings.isNullOrEmpty(byColumns)) {
                where += " ORDER BY " + byColumns;
            }
        }
        if (params == null || params.size() == 0) {
            return Db.paginate(start, pageSize, sql_columns, where);
        } else {

            return Db.paginate(start, pageSize, sql_columns, where, params.toArray());
        }
    }
}

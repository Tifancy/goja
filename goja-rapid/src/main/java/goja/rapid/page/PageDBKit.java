package goja.rapid.page;

import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import goja.db.DaoKit;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class PageDBKit {

//
//    /**
//     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
//     *
//     * @param model_name sql conf 中的 sqlGroup 的name
//     * @param pageDto    请求参数
//     * @return 分页数据
//     */
//    public static Page<Record> paginate(String model_name, PageDto pageDto) {
//        return paginate(SqlKit.sql(model_name + DaoKit.SQL_PIRFIX_WHERE), SqlKit.sql(model_name + DaoKit.SQL_PIRFIX_COLUMNS), SqlKit.sql(model_name + SQL_PIRFIX_ORDERS), pageDto);
//    }


    /**
     * 分页检索，默认按照id进行排序，需要指定datatables的请求参数。
     *
     * @param where         FROM WHERE 语句.
     * @param sql_columns   SELECT column sql 语句
     * @param pageDto       请求参数\
     * @param default_order 默认的排序字段，类似：ORDER BY id DESC
     * @return 分页数据
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

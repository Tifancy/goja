package goja.rapid.datatables;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import goja.IntPool;
import goja.tuples.Pair;
import goja.tuples.Triplet;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public final class DTCriterias implements Serializable {
    private static final long    serialVersionUID = -4728223524642774477L;
    /**
     * 匹配中括号的正则
     */
    private static final Pattern BRACKETS_PATTERN = Pattern.compile("\\[(.*?)\\]");

    private final DTSearch       search;
    private final int            start;
    private final int            length;
    private final List<DTColumn> columns;
    private final List<DTOrder>  order;
    private final int            draw;

    private final List<Triplet<String, Condition, Object>> params;

    private DTCriterias(DTSearch search, int start, int length, List<DTColumn> columns, List<DTOrder> order, int draw) {
        this.search = search;
        this.start = start;
        this.length = length;
        this.columns = columns;
        this.order = order;
        this.draw = draw;
        this.params = Lists.newArrayListWithCapacity(1);
    }


    public static DTCriterias criteriasWithRequest(HttpServletRequest request) {
        if (request != null) {

            String r_draw = request.getParameter(DTConstants.DT_DRAW);
            String r_start = request.getParameter(DTConstants.DT_START);
            String r_length = request.getParameter(DTConstants.DT_LENGTH);
            String r_search_value = request.getParameter(DTConstants.DT_SEARCH_VALUE);
            String r_search_regex = request.getParameter(DTConstants.DT_SEARCH_REGEX);

            int draw = Strings.isNullOrEmpty(r_draw) ? IntPool.ONE : Ints.tryParse(r_draw);
            int start = Strings.isNullOrEmpty(r_start) ? IntPool.ONE : Ints.tryParse(r_start);
            int length = Strings.isNullOrEmpty(r_length) ? IntPool.ONE : Ints.tryParse(r_length);
            start = start == IntPool.ZERO ? IntPool.ONE : start;

            DTSearch dtSearch = DTSearch.create(r_search_value, BooleanUtils.toBoolean(r_search_regex));

            List<DTColumn> dtColumns = Lists.newArrayList();
            List<DTOrder> dtOrders = Lists.newArrayList();

            List<Pair<Integer, String>> _orders = Lists.newArrayList();
            List<String> processed = Lists.newArrayList();

            final String method = request.getMethod();
            try {
                if (StringUtils.equals("GET", method)) {
                    Matcher matcher;
                    String p_index = null;
                    final Enumeration<String> parameterNames = request.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String param_name = parameterNames.nextElement();
                        // 列配置
                        matcher = BRACKETS_PATTERN.matcher(param_name);
                        if (matcher.find()) {
                            p_index = matcher.group(1);
                        }
                        if (StringUtils.startsWithIgnoreCase(param_name, DTConstants.DT_COLUMNS)) {

                            if (!processed.isEmpty() && processed.contains(p_index)) {
                                continue;
                            }
                            processed.add(p_index);
                            String column_data = request.getParameter("columns[" + p_index + "][data]");
                            if (Strings.isNullOrEmpty(column_data)) {
                                p_index = null;
                                continue;
                            }
                            String column_name = request.getParameter("columns[" + p_index + "][name]");
                            String column_searchable = request.getParameter("columns[" + p_index + "][searchable]");
                            String column_orderable = request.getParameter("columns[" + p_index + "][orderable]");
                            String column_search_value = request.getParameter("columns[" + p_index + "][search][value]");
                            String column_search_regex = request.getParameter("columns[" + p_index + "][search][regex]");


                            dtColumns.add(new DTColumn(column_data, column_name
                                    , BooleanUtils.toBoolean(column_searchable)
                                    , BooleanUtils.toBoolean(column_orderable)
                                    , DTSearch.create(column_search_value, BooleanUtils.toBoolean(column_search_regex))));


                        } else if (StringUtils.startsWithIgnoreCase(param_name, DTConstants.DT_ORDER)) {
                            int index = Ints.tryParse(p_index);
                            if (!_orders.isEmpty() && _orders.get(index) != null) {
                                continue;
                            }
                            String order_column_index = request.getParameter("order[" + p_index + "][column]");
                            String order_column_dir = request.getParameter("order[" + p_index + "][dir]");
                            Pair<Integer, String> _temp_order = Pair.with(Ints.tryParse(order_column_index), order_column_dir);
                            _orders.add(_temp_order);
                        }
                    }

                    if (!_orders.isEmpty()) {
                        for (Pair<Integer, String> pair : _orders) {
                            DTColumn column = dtColumns.get(pair.getValue0());
                            if (column == null) {
                                continue;
                            }
                            dtOrders.add(DTOrder.create(column.getData(), pair.getValue1()));
                        }
                    }
                }
            } finally {
                _orders = null;
                processed = null;
            }


            return new DTCriterias(dtSearch, start, length, dtColumns, dtOrders, draw);
        } else {
            return null;
        }
    }


    /**
     * Adding custom query condition and value
     *
     * @param field     query condition
     * @param condition condition
     * @param value     query condition
     */
    public void setParam(String field, Condition condition, Object value) {
        this.params.add(Triplet.with(field, condition, value));
    }

    /**
     * Adding custom query equal  value.
     *
     * @param field query condition
     * @param value query condition
     */
    public void setParam(String field, Object value) {
        this.setParam(field, Condition.Equal, value);
    }

    public List<Triplet<String, Condition, Object>> getParams() {
        return params;
    }

    public DTResponse response(String model_name) {

        Preconditions.checkNotNull(this, "datatable criterias is must be not null.");
        final Page<Record> datas = DTDao.paginate(model_name, this);
        return DTResponse.build(this, datas.getList(), datas.getTotalRow(), datas.getTotalRow());
    }

    /**
     * 支持对单个实体与Datatables插件的整合。
     *
     * @param model 实体类
     * @return 响应结果
     */
    public DTResponse response(Class<? extends Model> model) {
        return response(model, null);
    }

    /**
     * 对某个实体模型进行数据查询，并处理参数.
     *
     * @param model  实体类
     * @param params 参数
     * @return Datatables响应结果
     */
    public DTResponse response(Class<? extends Model> model, List<Object> params) {

        Preconditions.checkNotNull(this, "datatable criterias is must be not null.");
        final Page<Record> datas = DTDao.paginate(model, this, params);
        return DTResponse.build(this, datas.getList(), datas.getTotalRow(), datas.getTotalRow());
    }

    public DTResponse response(String model_name, List<Object> params) {

        Preconditions.checkNotNull(this, "datatable criterias is must be not null.");
        final Page<Record> datas = DTDao.paginate(model_name, this, params);
        return DTResponse.build(this, datas.getList(), datas.getTotalRow(), datas.getTotalRow());
    }


    public DTSearch getSearch() {
        return search;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public List<DTColumn> getColumns() {
        return columns;
    }

    public List<DTOrder> getOrder() {
        return order;
    }

    public int getDraw() {
        return draw;
    }

}

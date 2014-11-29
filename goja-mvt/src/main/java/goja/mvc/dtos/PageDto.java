/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.mvc.dtos;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import goja.GojaConfig;
import goja.StringPool;
import goja.db.DaoKit;
import org.apache.commons.lang3.StringUtils;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-08-17 16:11
 * @since JDK 1.6
 */
public class PageDto {

    public static final String P = "p";
    public static final String S = "s";

    public static final Integer DEFAULT_PAGE_SIZE = GojaConfig.getPropertyToInt("app.page.defaultsize", 10);

    public static final String APP_PAGE       = GojaConfig.getProperty("app.page", P);
    public static final String APP_PAGE_ORDER = GojaConfig.getProperty("app.page.order", "direction");
    public static final String APP_PAGE_SIZE  = GojaConfig.getProperty("app.page.size", S);
    public static final String APP_PAGE_SORT  = GojaConfig.getProperty("app.page.sort", "sort");

    public final  int       page;
    public final  int       pageSize;
    private final String    sort;
    private final Direction direction;
    private final boolean   hasSort;

    public final List<ReqParam> params = Lists.newArrayListWithCapacity(3);

    private final Map<String, Object> fq = Maps.newHashMap();

    public final List<Object> query_params = Lists.newArrayListWithCapacity(3);

    private final StringBuilder filter_url = new StringBuilder();


    public static PageDto create(com.jfinal.core.Controller controller) {

        String dir = controller.getPara(APP_PAGE_ORDER, "desc").toUpperCase();
        final Direction direction = Direction.valueOf(dir);

        final Enumeration<String> paraNames = controller.getParaNames();
        // exmaple add app.page.size=pager.pageSize into application.conf
        final int current_page = controller.getParaToInt(APP_PAGE, 1);
        // pager.pageNo
        final int page_size = controller.getParaToInt(APP_PAGE_SIZE, DEFAULT_PAGE_SIZE);

        String sort = controller.getPara(APP_PAGE_SORT);
        PageDto pageDto = Strings.isNullOrEmpty(sort) ? new PageDto(current_page, page_size) : new PageDto(current_page, page_size, sort, direction);
        while (paraNames.hasMoreElements()) {
            String p_key = paraNames.nextElement();
            if (!Strings.isNullOrEmpty(p_key) && StringUtils.startsWith(p_key, "s-")) {
                final String req_val = controller.getPara(p_key);
                if (!Strings.isNullOrEmpty(req_val)) {
                    String[] param_array = StringUtils.split(p_key, "-");
                    if (param_array != null && param_array.length >= 2) {

                        String name = param_array[1];
                        String condition = param_array.length == 2 ? PageDto.Condition.EQ.toString() : param_array[2];
                        condition = Strings.isNullOrEmpty(condition) ? PageDto.Condition.EQ.toString() : condition.toUpperCase();
                        if (StringUtils.equals(condition, PageDto.Condition.BETWEEN.toString())) {
                            String req_val2 = controller.getPara(StringUtils.replace(p_key, PageDto.Condition.BETWEEN.toString(), "AND"));
                            pageDto.putTwoVal(name, req_val, req_val2, condition);
                        } else {
                            pageDto.put(name, req_val, condition);
                        }
                    }
                }

            }
        }
        return pageDto;
    }

    private PageDto(int pageNo, int pageSize,
                    String sort,
                    Direction direction) {
        this.page = pageNo;
        this.pageSize = pageSize;
        this.sort = sort;
        this.direction = direction;
        hasSort = true;
    }

    private PageDto(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.sort = StringPool.EMPTY;
        this.direction = Direction.DESC;
        hasSort = false;
    }

    public void put(String key, String value, String condition) {
        putTwoVal(key, value, StringPool.EMPTY, condition);

        filter_url.append(StringPool.AMPERSAND).append(key).append(StringPool.EQUALS).append(value);
    }

    public void putTwoVal(String key, Object value, Object val2, String condition) {
        if (fq.containsKey(key)) {
            return;
        }
        final ReqParam reqParam = new ReqParam(key, condition);
        params.add(reqParam);
        switch (reqParam.condition) {
            case LIKE:
                query_params.add(DaoKit.like(String.valueOf(value)));
                break;
            case BETWEEN:
                query_params.add(value);
                query_params.add(val2);
                break;
            default:
                query_params.add(value);
                break;
        }
        fq.put(key, value);

        filter_url.append(StringPool.AMPERSAND).append(key).append(StringPool.EQUALS).append(value);
        if (val2 != null) {
            final String key_two = key + "2";
            fq.put(key_two, val2);
            filter_url.append(StringPool.AMPERSAND).append(key_two).append(StringPool.EQUALS).append(val2);
        }
    }

    public String getQueryUrl() {
        return filter_url.toString();
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<ReqParam> getParams() {
        return params;
    }

    public Map<String, Object> getFq() {
        return fq;
    }

    public List<Object> getQuery_params() {
        return query_params;
    }

    public String getSort() {
        return sort;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isHasSort() {
        return hasSort;
    }


    public static class ReqParam {
        public final String    key;
        public final Condition condition;

        public ReqParam(String key, String condition) {
            this.key = key;
            this.condition = Condition.valueOf(condition);
        }

        public String toSql() {
            switch (condition) {
                case LIKE:
                    return String.format(" AND %s %s ? ", key, condition.condition);
                case BETWEEN:
                    return String.format(" AND %s BETWEEN ? AND ? ", key);
                default:
                    return String.format(" AND %s %s ? ", key, condition.condition);
            }
        }
    }

    public static enum Direction {
        ASC,
        DESC
    }

    public static enum Condition {
        LIKE(" LIKE "),
        EQ(" = "),
        LT(" < "),
        LTEQ(" <= "),
        GTEQ(" >= "),
        BETWEEN(" BETWEEN "),
        GT(" > ");

        public final String condition;

        Condition(String condition) {
            this.condition = condition;
        }

    }
}

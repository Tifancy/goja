package goja.db;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import static goja.StringPool.EMPTY;
import static goja.StringPool.EQUALS;
import static goja.StringPool.LEFT_BRACKET;
import static goja.StringPool.QUESTION_MARK;
import static goja.StringPool.RIGHT_BRACKET;
import static goja.StringPool.SINGLE_QUOTE;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public abstract class SqlQuery {

    protected final List<Object> params;

    protected SqlQuery() {
        params = Lists.newArrayList();
    }

    public static String quote(String str) {
        return SINGLE_QUOTE + str.replace(SINGLE_QUOTE, "\\'") + SINGLE_QUOTE;
    }

    public static String inlineParam(Object param) {
        if (param == null) return "NULL";

        String str;
        if (param instanceof String) str = quote(param.toString());
        else if (param instanceof Iterable<?>) {
            Concat list = new Concat(LEFT_BRACKET, ", ", RIGHT_BRACKET);
            for (Object p : (Iterable<?>) param) list.append(inlineParam(p));
            str = list.toString();
        } else if (param instanceof Object[]) {
            Concat list = new Concat(LEFT_BRACKET, ", ", RIGHT_BRACKET);
            for (Object p : (Object[]) param) list.append(inlineParam(p));
            str = list.toString();
        } else if (param instanceof Enum<?>) {
            str = quote(param.toString());
        } else str = param.toString();
        return str;
    }

    public static String whereIn(String column, Object param) {
        String value = inlineParam(param);
        if (value.length() == 0) return value;

        String operator;
        if (param instanceof Object[]) {
            operator = " IN ";
        } else if (param instanceof Iterable<?>) {
            operator = " IN ";
        } else {
            operator = EQUALS;
        }

        return column + operator + value;
    }


    public SqlQuery param(Object obj) {
        params.add(obj);
        return this;
    }

    public SqlQuery params(Object... objs) {
        Collections.addAll(params, objs);
        return this;
    }

    public List<Object> getParams() {
        return params;
    }

    public int paramCurrentIndex() {
        return params.size() + 1;
    }

    public String pmark() {
        return QUESTION_MARK + Integer.toString(paramCurrentIndex());
    }

    public String pmark(int offset) {
        return QUESTION_MARK + Integer.toString(paramCurrentIndex() + offset);
    }

    public static class Concat {
        private String prefix, separator, suffix;
        private String defaultValue;
        private String expr;

        public Concat(String prefix, String separator, String suffix) {
            this.prefix = prefix;
            this.separator = separator;
            this.suffix = suffix;
            this.defaultValue = EMPTY;
            this.expr = EMPTY;
        }

        public Concat(String prefix, String separator) {
            this(prefix, separator, EMPTY);
        }

        public Concat(Concat src) {
            this.prefix = src.prefix;
            this.separator = src.separator;
            this.suffix = src.suffix;
            this.defaultValue = src.defaultValue;
            this.expr = src.expr;
        }

        public Concat defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Concat prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Concat separator(String separator) {
            this.separator = separator;
            return this;
        }

        public Concat append(Object obj) {
            final String text;
            if (obj != null) {
                String objStr = obj.toString();
                if (objStr.length() > 0) text = objStr;
                else text = defaultValue;
            } else text = defaultValue;

            if (text != null) {
                if (expr.length() > 0) {
                    if (separator == null) throw new NullPointerException();
                    expr += separator;
                }
                expr += text;
            }
            return this;
        }

        public Concat add(String... texts) {
            for (String text : texts) append(text);
            return this;
        }

        public boolean isEmpty() {
            return expr.length() <= 0;
        }

        @Override
        public String toString() {
            if (isEmpty()) return EMPTY;
            if (prefix == null || suffix == null) throw new NullPointerException();
            return prefix + expr + suffix;
        }
    }
}

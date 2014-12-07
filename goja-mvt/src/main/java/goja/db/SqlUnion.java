package goja.db;

import goja.StringPool;

import static goja.StringPool.EMPTY;
import static goja.StringPool.LEFT_BRACKET;
import static goja.StringPool.RIGHT_BRACKET;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class SqlUnion extends SqlQuery {
    private final Concat union;
    private final Concat orderBy;
    private final Concat limit;

    public SqlUnion() {
        union = new Concat(EMPTY, null);
        orderBy = new Concat("ORDER BY ", ", ").defaultValue(null);
        limit = new Concat("LIMIT ", null);
    }

    public SqlUnion(SqlUnion src) {
        union = new Concat(src.union);
        orderBy = new Concat(src.orderBy);
        limit = new Concat(src.limit);

        params.addAll(src.getParams());
    }

    @Override public SqlUnion param(Object obj) { super.param(obj); return this; }
    @Override public SqlUnion params(Object ... objs) { super.params(objs); return this; }

    public SqlUnion orderBy(String ... expr) { orderBy.add(expr); return this; }
    public SqlUnion limit(long lines) { limit.append(lines); return this; }
    public SqlUnion limit(long offset, long lines) { limit.append(offset +", "+ lines); return this; }

    private void unionSep(String separator, SqlSelect ... expr) {
        for (SqlSelect query : expr) {
            String sql = query.toString();
            if (sql.length()>0) sql = LEFT_BRACKET + sql + RIGHT_BRACKET;
            union.separator(separator).append(sql);
            params.addAll(query.getParams());
        }
    }
    public SqlUnion union(SqlSelect ... expr) { unionSep(" UNION ", expr); return this; }
    public SqlUnion unionAll(SqlSelect ... expr) { unionSep(" UNION ALL ", expr); return this; }

    @Override
    public String toString() {
        return union.isEmpty() ? EMPTY : new Concat(EMPTY, " ").defaultValue(null)
                .append(union)
                .append(orderBy)
                .append(limit)
                .toString();
    }
}

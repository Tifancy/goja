package goja.rapid.datatables;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public enum Condition {
    NotEqual(" <> ?"),
    Equal(" = ?"),
    IsNotNull(" is not null"),
    IsNull(" is null"),
    LessThan(" < ?"),
    LessThanEquals(" <= ?"),
    GreaterThan(" > ?"),
    GreaterThanEquals(" >= ?"),
    Between(" BETWEEN ? AND ?"),
    Like(" LIKE ?");

    public final String condition;

    Condition(String condition) {
        this.condition = condition;
    }
}

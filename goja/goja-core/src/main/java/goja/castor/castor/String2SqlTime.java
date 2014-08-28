
package goja.castor.castor;


import org.apache.commons.lang3.StringUtils;

public class String2SqlTime extends DateTimeCastor<String, java.sql.Time> {

    @Override
    public java.sql.Time cast(String src, Class<?> toType, String... args) {
        if (StringUtils.isBlank(src))
            return null;
        return new java.sql.Time(toDate(src).getTime());
    }

}
/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.activerecord.dialect;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * OracleDialect.
 */
public class OracleDialect extends Dialect {
    private static final Logger logger = LoggerFactory.getLogger(OracleDialect.class);

    public String forTableBuilderDoBuild(String tableName) {
        return "SELECT * FROM " + tableName + " where rownum < 1";
    }

    // insert into table (id,name) values(seq.nextval, ？)
    public void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("INSERT INTO ").append(table.getName()).append("(");
        StringBuilder temp = new StringBuilder(") VALUES(");
        String pKey = table.getPrimaryKey();
        int count = 0;
        for (Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (table.hasColumnLabel(colName)) {
                if (count++ > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append(colName);
                Object value = e.getValue();
                if (value instanceof String && colName.equalsIgnoreCase(pKey) && ((String) value).endsWith(".nextval")) {
                    temp.append(value);
                } else {
                    temp.append("?");
                    paras.add(value);
                }
            }
        }
        sql.append(temp.toString()).append(")");
    }

    public String forModelDeleteById(Table table) {
        String pKey = table.getPrimaryKey();
        StringBuilder sql = new StringBuilder(45);
        sql.append("delete from ");
        sql.append(table.getName());
		sql.append(" where ").append(pKey).append(" = ?");
		return sql.toString();
	}
	
	public void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag, String pKey, Object id, StringBuilder sql, List<Object> paras) {
		sql.append("update ").append(table.getName()).append(" set ");
		for (Entry<String, Object> e : attrs.entrySet()) {
			String colName = e.getKey();
			if (!pKey.equalsIgnoreCase(colName) && modifyFlag.contains(colName) && table.hasColumnLabel(colName)) {
				if (paras.size() > 0)
					sql.append(", ");
				sql.append(colName).append(" = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where ").append(pKey).append(" = ?");
		paras.add(id);
	}
	
	public String forModelFindById(Table table, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		}
		else {
			String[] columnsArray = columns.split(",");
			for (int i=0; i<columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append(columnsArray[i].trim());
			}
		}
		sql.append(" from ");
		sql.append(table.getName());
		sql.append(" where ").append(table.getPrimaryKey()).append(" = ?");
		return sql.toString();
	}
	
	public String forDbFindById(String tableName, String primaryKey, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		}
		else {
			String[] columnsArray = columns.split(",");
			for (int i=0; i<columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append(columnsArray[i].trim());
			}
		}
		sql.append(" from ");
		sql.append(tableName.trim());
		sql.append(" where ").append(primaryKey).append(" = ?");
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String primaryKey) {
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName.trim());
		sql.append(" where ").append(primaryKey).append(" = ?");
		return sql.toString();
	}
	
	public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {
		sql.append("insert into ");
		sql.append(tableName.trim()).append("(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");
		
		int count = 0;
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			if (count++ > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append(e.getKey());
			
			Object value = e.getValue();
			if(value instanceof String && (((String)value).endsWith(".nextval"))) {
			    temp.append(value);
			}else{
				temp.append("?");
				paras.add(value);
			}
		}
		sql.append(temp.toString()).append(")");
	}
	
	public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {
		sql.append("update ").append(tableName.trim()).append(" set ");
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName)) {
				if (paras.size() > 0) {
					sql.append(", ");
				}
				sql.append(colName).append(" = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where ").append(primaryKey).append(" = ?");
		paras.add(id);
	}
	
	public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		int satrt = (pageNumber - 1) * pageSize + 1;
		int end = pageNumber * pageSize;
		sql.append("select * from ( select row_.*, rownum rownum_ from (  ");
		sql.append(select).append(" ").append(sqlExceptSelect);
		sql.append(" ) row_ where rownum <= ").append(end).append(") table_alias");
		sql.append(" where table_alias.rownum_ >= ").append(satrt);
	}
	
	public boolean isOracle() {
		return true;
	}
	
	public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        /* # edit by sogyf. */
        /* @description:  when dev model print sql parm*/
        boolean show_param = logger.isDebugEnabled();
        final int param_size = paras.size();
        if (show_param) {
            logger.debug("Sql param size : {}", param_size == 0 ? " Empty" : param_size);

            for (int i = 0; i < param_size; i++) {
                final Object value = paras.get(i);
                if (value instanceof DateTime)
                    pst.setTimestamp(i + 1, new Timestamp(((DateTime) value).getMillis()));
                else if (value instanceof java.sql.Date)
                    pst.setDate(i + 1, (java.sql.Date) value);
                else if (value instanceof Date)
                    pst.setTimestamp(i + 1, new Timestamp(((Date)value).getTime()));
                else
                    pst.setObject(i + 1, value);
                logger.debug("   param index: {}, param type: {}, param value: {}. ", i + 1, (value == null ? "null" : value.getClass().getSimpleName()), value);
            }
            logger.debug("Sql param end!");
        } else {
            for (int i = 0; i < param_size; i++) {
                Object value = paras.get(i);
                if (value instanceof DateTime)
                    pst.setTimestamp(i + 1, new Timestamp(((DateTime) value).getMillis()));
                else if (value instanceof java.sql.Date)
                    pst.setDate(i + 1, (java.sql.Date) value);
                else if (value instanceof Date)
                    pst.setTimestamp(i + 1, new Timestamp(((Date)value).getTime()));
                else
                    pst.setObject(i + 1, value);
            }
        }
        /* # end edited. */


    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
       /* # edit by sogyf. */
        /* @description:  when dev model print sql parm*/
        boolean show_param = logger.isDebugEnabled();
        final int param_size = paras.length;
        if (show_param) {
            logger.debug("Sql param size : {}", param_size == 0 ? " Empty" : param_size);

            for (int i = 0; i < param_size; i++) {
                final Object value = paras[i];
                if (value instanceof DateTime)
                    pst.setTimestamp(i + 1, new Timestamp(((DateTime) value).getMillis()));
                else if (value instanceof java.sql.Date)
                    pst.setDate(i + 1, (java.sql.Date) value);
                else if (value instanceof java.sql.Timestamp)
                    pst.setTimestamp(i + 1, (java.sql.Timestamp)value);
                else if (value instanceof Date)
                    pst.setTimestamp(i + 1, new Timestamp(((Date)value).getTime()));
                else
                    pst.setObject(i + 1, value);
                logger.debug("   param index: {}, param type: {}, param value: {}. ", i + 1, (value == null ? "null" : value.getClass().getSimpleName()), value);
            }
            logger.debug("Sql param end!");
        } else {
            for (int i = 0; i < param_size; i++) {
                Object value = paras[i];
                if (value instanceof DateTime)
                    pst.setTimestamp(i + 1, new Timestamp(((DateTime) value).getMillis()));
                else if (value instanceof java.sql.Date)
                    pst.setDate(i + 1, (java.sql.Date) value);
                else if (value instanceof java.sql.Timestamp)
                    pst.setTimestamp(i + 1, (java.sql.Timestamp)value);
                else if (value instanceof Date)
                    pst.setTimestamp(i + 1, new Timestamp(((Date)value).getTime()));
                else
                    pst.setObject(i + 1, value);
            }
        }
        /* # end edited. */
	}
	
	public String getDefaultPrimaryKey() {
		return "id";
	}
}

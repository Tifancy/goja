package goja.db;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SqlSelectTest {
    @Test
    public void testSelect() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").toString();
        String dest = "SELECT f1,f2 FROM t1";
        assertThat(dest, equalTo(s));
    }


    @Test
    public void testFrom() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").toString();
        String dest = "SELECT f1,f2 FROM t1";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testInnerJoin() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").innerJoin("t1=t2").toString();
        String dest = "SELECT f1,f2 FROM t1 INNER JOIN t1=t2";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testLeftJoin() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").leftJoin("t1=t2").toString();
        String dest = "SELECT f1,f2 FROM t1 LEFT JOIN t1=t2";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testWhere() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").where("f1=1").toString();
        String dest = "SELECT f1,f2 FROM t1 WHERE f1=1";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testAndWhere() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").where("f1=1").andWhere("f2=?").toString();
        String dest = "SELECT f1,f2 FROM t1 WHERE f1=1 AND f2=?";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testOrWhere() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").where("f1=1").orWhere("f2=?").toString();
        String dest = "SELECT f1,f2 FROM t1 WHERE f1=1 OR f2=?";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testGroupBy() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").groupBy("f2").toString();
        String dest = "SELECT f1,f2 FROM t1 GROUP BY f2";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testOrderBy() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").orderBy("f2").toString();
        String dest = "SELECT f1,f2 FROM t1 ORDER BY f2";
        assertThat(dest, equalTo(s));
    }

    @Test
    public void testLimit() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").limit(10).toString();
        String dest = "SELECT f1,f2 FROM t1 LIMIT 10";
        assertThat(dest, equalTo(s));
    }


    @Test
    public void testWhereIn() throws Exception {
        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").where(SqlQuery.whereIn("f1", Lists.newArrayList(1, 2, 3))).toString();
        String dest = "SELECT f1,f2 FROM t1 WHERE f1 IN (1, 2, 3)";
        assertThat(dest, equalTo(s));
    }


    @Test
    public void testFindBy() throws Exception {

        SqlSelect sqlSelect = new SqlSelect();
        String s = sqlSelect.select("f1,f2").from("t1").where(FindBy.findBy("ByF1Equal")).toString();
        String dest = "SELECT f1,f2 FROM t1 WHERE f1 = ?";
        assertThat(dest, equalTo(s));
    }
}
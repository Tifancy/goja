package goja.rapid.db;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static goja.rapid.db.FindBy.findBy;

public class FindByTest {

    @Test
    public void testMain() throws Exception {

        MatcherAssert.assertThat("title = ?", Matchers.equalTo(findBy("byTitle")));
        MatcherAssert.assertThat("LOWER(title) like ?", Matchers.equalTo(findBy("byTitleLike")));
        MatcherAssert.assertThat("author is null", Matchers.equalTo(findBy("byAuthorIsNull")));
        MatcherAssert.assertThat("LOWER(title) like ? AND author = ?", Matchers.equalTo(findBy("byTitleLikeAndAuthor")));
        MatcherAssert.assertThat("name = ? ORDER BY name", Matchers.equalTo(findBy("ByNameOrderByName")));
        MatcherAssert.assertThat("name < ?", Matchers.equalTo(findBy("ByNameLessThan")));
        MatcherAssert.assertThat("name <= ?", Matchers.equalTo(findBy("ByNameLessThanEquals")));
        MatcherAssert.assertThat("name > ?", Matchers.equalTo(findBy("ByNameGreaterThan")));
        MatcherAssert.assertThat("name >= ?", Matchers.equalTo(findBy("ByNameGreaterThanEquals")));
        MatcherAssert.assertThat("LOWER(name) like LOWER(?)", Matchers.equalTo(findBy("ByNameIlike")));
        MatcherAssert.assertThat("name like ?", Matchers.equalTo(findBy("ByNameElike")));
        MatcherAssert.assertThat("name <> ?", Matchers.equalTo(findBy("ByNameNotEqual")));
        MatcherAssert.assertThat("name ? AND name ?", Matchers.equalTo(findBy("ByNameBetween")));
        MatcherAssert.assertThat("name is not null", Matchers.equalTo(findBy("ByNameIsNotNull")));

    }

    @Test
    public void testBetween() throws Exception {
        System.out.println("findBy(\"byDateBetween\") = " + findBy("byDateBetween"));
        System.out.println("findBy(\"byDateIlike\") = " + findBy("byDateIlike"));

    }
}
package goja.db;

import org.junit.Test;

import static goja.db.FindBy.findBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FindByTest {

    @Test
    public void testFindBy() throws Exception {

        assertThat("title = ?", equalTo(findBy("byTitle")));
        assertThat("LOWER(title) like ?", equalTo(findBy("byTitleLike")));
        assertThat("author is null", equalTo(findBy("byAuthorIsNull")));
        assertThat("LOWER(title) like ? AND author = ?", equalTo(findBy("byTitleLikeAndAuthor")));
        assertThat("name = ? ORDER BY name", equalTo(findBy("ByNameOrderByName")));
        assertThat("name < ?", equalTo(findBy("ByNameLessThan")));
        assertThat("name <= ?", equalTo(findBy("ByNameLessThanEquals")));
        assertThat("name > ?", equalTo(findBy("ByNameGreaterThan")));
        assertThat("name >= ?", equalTo(findBy("ByNameGreaterThanEquals")));
        assertThat("LOWER(name) like LOWER(?)", equalTo(findBy("ByNameIlike")));
        assertThat("name like ?", equalTo(findBy("ByNameElike")));
        assertThat("name <> ?", equalTo(findBy("ByNameNotEqual")));
        assertThat("name ? AND name ?", equalTo(findBy("ByNameBetween")));
        assertThat("name is not null", equalTo(findBy("ByNameIsNotNull")));
    }
}
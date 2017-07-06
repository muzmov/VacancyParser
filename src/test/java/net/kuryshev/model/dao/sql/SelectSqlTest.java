package net.kuryshev.model.dao.sql;

import org.junit.Assert;
import org.junit.Test;

public class SelectSqlTest {

    @Test
    public void generate_noFilters_Test() {
        String[] columns = {};
        String[] filters = {};
        Sql selectSql = new SelectSql("Vacancies", columns, filters);
        Assert.assertEquals("SELECT * FROM Vacancies", selectSql. generate());
    }

    @Test
    public void generate_correctTwoFilters_Test() {
        String[] columns = {"column_1", "column_2"};
        String[] filters = {"filter_1", "filter_2"};
        Sql selectSql = new SelectSql("Vacancies", columns, filters);
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE 'filter_1' OR column_2 LIKE 'filter_2'", selectSql. generate());
    }
    @Test
    public void generate_correctOneFilter_Test() {
        String[] columns = {"column_1"};
        String[] filters = {"filter_1"};
        Sql selectSql = new SelectSql("Vacancies", columns, filters);
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE 'filter_1'", selectSql. generate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void generate_incorrectFilters_Test() {
        String[] columns = {};
        String[] filters = {"filter_1", "filter_2"};
        Sql selectSql = new SelectSql("Vacancies", columns, filters);
        Assert.assertEquals("SELECT * FROM Vacancies", selectSql. generate());
    }
}
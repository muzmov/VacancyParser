package net.kuryshev.model.dao.sql;

import org.junit.Assert;
import org.junit.Test;

public class SelectSqlTest {

    @Test
    public void generate_noFilters_Test() {
        Sql selectSql = new SelectSql("Vacancies");
        Assert.assertEquals("SELECT * FROM Vacancies", selectSql. generate());
    }

    @Test
    public void generate_simpleTwoFilters_Test() {
        String[] columns = {"column_1", "column_2"};
        String[] filters = {"filter_1", "filter_2"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE 'filter_1' OR column_2 LIKE 'filter_2'", selectSql. generate());
    }

    @Test
    public void generate_simpleOneFilter_Test() {
        String[] columns = {"column_1"};
        String[] filters = {"filter_1"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE 'filter_1'", selectSql. generate());
    }

    @Test
    public void generate_complexMultipleWordsFilter_Test() {
        String[] columns = {"column_1"};
        String[] filters = {"%filter_1 filter_2%"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE '%filter_1%' AND column_1 LIKE '%filter_2%'", selectSql. generate());
    }

    @Test
    public void generate_complexMultipleWordsTwoFilters_Test() {
        String[] columns = {"column_1", "column_2"};
        String[] filters = {"%filter_1 filter_2%", "%filter_3 filter_4%"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE '%filter_1%' AND column_1 LIKE '%filter_2%'" +
                " OR column_2 LIKE '%filter_3%' AND column_2 LIKE '%filter_4%'", selectSql. generate());
    }


    @Test
    public void generate_complexSingleWordComplexFilter_Test() {
        String[] columns = {"column_1"};
        String[] filters = {"%filter_1%"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies WHERE column_1 LIKE '%filter_1%'", selectSql. generate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void generate_incorrectFilters_Test() {
        String[] columns = {};
        String[] filters = {"filter_1", "filter_2"};
        SelectSql selectSql = new SelectSql("Vacancies");
        selectSql.setFilters(columns, filters, "OR");
        Assert.assertEquals("SELECT * FROM Vacancies", selectSql. generate());
    }
}
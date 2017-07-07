package net.kuryshev.model.dao.sql;

import org.junit.Assert;
import org.junit.Test;

public class InsertSqlTest {

    @Test(expected = IllegalArgumentException.class)
    public void generate_IncorrectTable_Test() {
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8"};
        Sql insertSql = new InsertSql("Incorrect Table", values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generate_IncorrectValues_Test() {
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8"};
        Sql insertSql = new InsertSql("Companies", values);
    }

    @Test
    public void generate_CompaniesSql_Test() {
        String[] values = {"1", "2", "3", "4"};
        Sql insertSql = new InsertSql("Companies", values);
        Assert.assertEquals("INSERT INTO Companies (name, url, rating, reviews_url) VALUES ('1', '2', 3, '4')", insertSql.generate() );
    }

    @Test
    public void generate_UsersSql_Test() {
        String[] values = {"1", "2", "3"};
        Sql insertSql = new InsertSql("Users", values);
        Assert.assertEquals("INSERT INTO Users (login, password, role) VALUES ('1', '2', '3')", insertSql.generate() );
    }

    @Test
    public void generate_VacanciesSql_Test() {
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8"};
        Sql insertSql = new InsertSql("Vacancies", values);
        Assert.assertEquals("INSERT INTO Vacancies (title, description, url, site_name, city, company, salary, rating) VALUES ('1', '2', '3', '4', '5', '6', '7', 8)", insertSql.generate() );
    }

}
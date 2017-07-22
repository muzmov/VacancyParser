package net.kuryshev.model.dao;

import net.kuryshev.model.SearchParams;
import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class VacancyDaoJdbcTest {
    private static final VacancyDaoJdbc dao = new VacancyDaoJdbc();

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/?useSSL=false";
    private static final String user = "root";
    private static final String password = "password";
    private static final String[] SET_UP_SQL = {"CREATE SCHEMA IF NOT EXISTS vacancyparser_test",
    "USE vacancyparser_test",
    "SET SQL_SAFE_UPDATES = 0",
    "DROP TABLE IF EXISTS Vacancies",
    "CREATE TABLE Vacancies (\n" + "  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" + "  title       TEXT NOT NULL,\n" + "  description TEXT,\n" + "  url         VARCHAR(255) NOT NULL UNIQUE,\n" + "  site_name   VARCHAR(32) NOT NULL ,\n" + "  city        VARCHAR(32) NOT NULL ,\n" + "  company  VARCHAR(255) NOT NULL ,\n" + "  salary      VARCHAR(255),\n" + "  rating      DOUBLE\n" + ")",
    "DROP TABLE IF EXISTS Companies",
    "CREATE TABLE Companies (\n" + " name        VARCHAR(255) NOT NULL PRIMARY KEY ,\n" + "  url         VARCHAR(255) NOT NULL,\n" + "  rating      DOUBLE,\n" + "  reviews_url TEXT\n" + ")"};
    private static final String TEAR_DOWN_SQL = "DROP SCHEMA IF EXISTS vacancyparser_test";

    @Before
    public void setUp() {
        dao.setProperties("src/test/resources/dao.properties");
        try (Connection con = DriverManager.getConnection(JDBC_URL, user, password);
            Statement stmt = con.createStatement())
        {
            for (int i = 0; i < SET_UP_SQL.length; i++) {
                stmt.executeUpdate(SET_UP_SQL[i]);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @Test
    public void selectAllFromEmpty() throws Exception {
        dao.deleteAll();
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void addOneSelectAll() {
        dao.deleteAll();
        Vacancy vacancy = new Vacancy("test", "test", "test","test","test","test", 5, new Company("test", "test", "test", 4.4));
        dao.add(vacancy);
        List<Vacancy> result = dao.selectAll();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(vacancy, result.get(0));
    }

    @Test
    public void add100SelectAllTestTime() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i,"test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        long start = System.currentTimeMillis();
        dao.addAll(vacancies);
        long finish = System.currentTimeMillis();
        System.out.println("100 vacancies added in " + (finish - start));
        start = System.currentTimeMillis();
        vacancies = dao.selectAll();
        finish = System.currentTimeMillis();
        System.out.println("100 vacancies selected in " + (finish - start));
    }

    @Test
    public void add100SelectAllTestSize() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i,"test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectAll();
        Assert.assertEquals(100, vacancies.size());
    }

    @Test
    public void add100SelectAllTestCompanies() {
        List<Vacancy> vacancies = new ArrayList<>();
        List<Company> companies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 10; i++) {
            Company company = new Company("test" + i,"test" + i,"test" + i, i);
            companies.add(company);
        }
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i,"test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, companies.get(i / 10));
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        List<Vacancy> results = dao.selectAll();
        Assert.assertEquals(vacancies, results);
    }

    @Test
    public void add100SelectContainingInTitle() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("1", SearchParams.SEARCH_IN_TITLE);
        Assert.assertEquals(19, vacancies.size());
    }

    @Test
    public void add100SelectContainingInDescription() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("1", SearchParams.SEARCH_IN_DESCRIPTION);
        Assert.assertEquals(19, vacancies.size());
    }

    @Test
    public void add100SelectContainingInTitleOrDescription() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("1", SearchParams.SEARCH_IN_TITLE_AND_DESCRIPTION);
        Assert.assertEquals(19, vacancies.size());
    }

    @Test
    public void add100WithSpecialSymbolsSelectAll() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test'" + i, "test'" + i,"test'" + i,"test'" + i,"test'" + i,"test'" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectAll();
        Assert.assertEquals(100, vacancies.size());
    }

    @Test
    public void add100WithSpecialSymbolsSelectContaining() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test'" + i, "test'" + i,"test'" + i,"test'" + i,"test'" + i,"test'" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("'1", SearchParams.SEARCH_IN_TITLE);
        Assert.assertEquals(11, vacancies.size());
    }

    @Test
    public void add100DeleteAllSelectAll() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
        }
        dao.addAll(vacancies);
        dao.deleteAll();
        vacancies = dao.selectAll();
        Assert.assertEquals(0, vacancies.size());
    }


    @After
    public void tearDown() {

        try (Connection con = DriverManager.getConnection(JDBC_URL, user, password);
            Statement stmt = con.createStatement())
        {
            stmt.executeUpdate(TEAR_DOWN_SQL);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
}
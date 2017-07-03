package net.kuryshev.model.dao;

import net.kuryshev.model.SearchParams;
import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1 on 27.06.2017.
 */
public class VacancyDaoJdbcTest {
    private static final VacancyDaoJdbc dao = new VacancyDaoJdbc();

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String JDBC_TESTING_URL = "jdbc:mysql://localhost:3306/vacancyparser_test?useSSL=false";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/?useSSL=false";
    private static final String user = "root";
    private static final String password = "password";
    private static final String[] SET_UP_SQL = {"CREATE SCHEMA IF NOT EXISTS vacancyparser_test",
    "USE vacancyparser_test",
    "SET SQL_SAFE_UPDATES = 0",
    "DROP TABLE IF EXISTS Vacancies",
    "CREATE TABLE Vacancies (\n" + "  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" + "  title       TEXT NOT NULL,\n" + "  description TEXT,\n" + "  url         TEXT NOT NULL ,\n" + "  site_name   VARCHAR(32) NOT NULL ,\n" + "  city        VARCHAR(32) NOT NULL ,\n" + "  company  VARCHAR(255) NOT NULL ,\n" + "  salary      VARCHAR(255),\n" + "  rating      DOUBLE\n" + ")",
    "DROP TABLE IF EXISTS Companies",
    "CREATE TABLE Companies (\n" + " name        VARCHAR(255) NOT NULL PRIMARY KEY ,\n" + "  url         VARCHAR(255) NOT NULL,\n" + "  rating      DOUBLE,\n" + "  reviews_url TEXT\n" + ")",
            "INSERT INTO Companies (name, url, rating, reviews_url)\n" + "VALUES\n" + "  ('GlowByte', 'http://glowbyte.com', 4.4, 'http://linktoreviews.ru'),\n" + "  ('I-TECO', 'http://i-teco.com', 4.4, 'http://linktoreviews.ru'),\n" + "  ('Rostelecom', 'http://rostele.com', 4.4, 'http://linktoreviews.ru')",
            "INSERT INTO Vacancies (title, description, url, site_name, city, company, salary, rating)\n" + "VALUES\n" + "  ('Vacancy #1', 'primitive description', 'http://hh/.ru', 'hh.ru', 'Moscow', 'GlowByte', '100 000 - 150 000 roubles', 4.4),\n" + "  ('Vacancy #2', 'primitive description', 'http://hh/.ru', 'hh.ru', 'Moscow', 'I-TECO', '10 000 - 150 000 roubles', 4.5),\n" + "  ('Vacancy #3', 'primitive description', 'http://hh/.ru', 'hh.ru', 'Moscow', 'Rostelecom', '10 000 - 15 000 roubles', 4.6)"};
    private static final String TEAR_DOWN_SQL = "DROP SCHEMA IF EXISTS vacancyparser_test";

    @Before
    public void setUp() {
        Class clazz = dao.getClass();
        try {
            Field jdbcUrl = clazz.getDeclaredField("JDBC_URL");
            jdbcUrl.setAccessible(true);
            jdbcUrl.set(dao, JDBC_TESTING_URL);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Connection con = null;
        Statement stmt = null;

        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            for (int i = 0; i < 9; i++) {
                stmt.executeUpdate(SET_UP_SQL[i]);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    @Test
    public void selectAll_FromHaving3_Test() throws Exception {
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertNotNull(result.get(0));
        Assert.assertNotNull(result.get(0).getCompany());
    }

    @Test
    public void selectAll_FromEmpty_Test() throws Exception {
        dao.deleteAll();
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void addOne_SelectAll_Test() {
        dao.deleteAll();
        Vacancy vacancy = new Vacancy("test", "test", "test","test","test","test", 5, new Company("test", "test", "test", 4.4));
        dao.add(vacancy);
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), vacancy);
    }

    @Test
    public void add100_SelectAll_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i,"test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectAll();
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 100);
    }

    @Test
    public void addMany_SearchContaining_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("1", SearchParams.SEARCH_IN_TITLE);
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 19);
    }

    @Test
    public void addMany_DeleteAll_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company());
        }
        dao.addAll(vacancies);
        dao.deleteAll();
        vacancies = dao.selectAll();
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 0);
    }


    @After
    public void tearDown() {

        Connection con = null;
        Statement stmt = null;

        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(TEAR_DOWN_SQL);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }
}
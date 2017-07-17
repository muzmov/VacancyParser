package net.kuryshev.model.dao;

import net.kuryshev.model.entity.Company;
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

public class CompanyDaoJdbcTest {
    private static final CompanyDaoJdbc dao = new CompanyDaoJdbc();

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String JDBC_TESTING_URL = "jdbc:mysql://localhost:3306/vacancyparser_test?useSSL=false";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/?useSSL=false";
    private static final String user = "root";
    private static final String password = "password";
    private static final String[] SET_UP_SQL = {"CREATE SCHEMA IF NOT EXISTS vacancyparser_test",
            "USE vacancyparser_test",
            "SET SQL_SAFE_UPDATES = 0",
            "DROP TABLE IF EXISTS Vacancies",
            "CREATE TABLE Vacancies (\n" + "  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" + "  title       TEXT NOT NULL,\n" + "  description TEXT,\n" + "  url         VARCHAR(255) NOT NULL UNIQUE ,\n" + "  site_name   VARCHAR(32) NOT NULL ,\n" + "  city        VARCHAR(32) NOT NULL ,\n" + "  company  VARCHAR(255) NOT NULL ,\n" + "  salary      VARCHAR(255),\n" + "  rating      DOUBLE\n" + ")",
            "DROP TABLE IF EXISTS Companies",
            "CREATE TABLE Companies (\n" + " name        VARCHAR(255) NOT NULL PRIMARY KEY ,\n" + "  url         VARCHAR(255) NOT NULL,\n" + "  rating      DOUBLE,\n" + "  reviews_url TEXT\n" + ")"};
    private static final String TEAR_DOWN_SQL = "DROP SCHEMA IF EXISTS vacancyparser_test";

    @Before
    public void setUp() {
        Class clazz = dao.getClass();
        try {
            Field jdbcUrl = clazz.getDeclaredField("JDBC_URL");
            jdbcUrl.setAccessible(true);
            jdbcUrl.set(dao, JDBC_TESTING_URL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

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
    public void selectAllFromEmpty() {
        dao.deleteAll();
        List<Company> companies = dao.selectAll();
        Assert.assertEquals(0, companies.size());
    }

    @Test
    public void addOneSelectAll() {
        dao.deleteAll();
        Company company = new Company("test", "test", "test", 1);
        dao.add(company);
        List<Company> companies = dao.selectAll();
        Assert.assertEquals(1, companies.size());
        Assert.assertEquals(company, companies.get(0));
    }

    @Test
    public void add100SelectAll() {
        dao.deleteAll();
        List<Company> addedCompanies = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Company company = new Company("test" + i, "test" + i, "test" + i, 1);
        }
        dao.addAll(addedCompanies);
        List<Company> companies = dao.selectAll();
        Assert.assertEquals(100, companies.size());
        Assert.assertEquals(addedCompanies, companies);
    }

    @Test
    public void add100DeleteAll() {
        dao.deleteAll();
        List<Company> addedCompanies = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Company company = new Company("test" + i, "test" + i, "test" + i, 1);
        }
        dao.addAll(addedCompanies);
        dao.deleteAll();
        List<Company> companies = dao.selectAll();
        Assert.assertEquals(0, companies.size());
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
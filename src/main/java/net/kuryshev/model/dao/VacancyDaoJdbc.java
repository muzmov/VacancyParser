package net.kuryshev.model.dao;


import net.kuryshev.model.Vacancy;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;


public class VacancyDaoJdbc implements VacancyDao {

    private static Logger logger = Logger.getLogger(getClassName());

    //TODO: move settings to property file
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private static final String user = "root";
    private static final String password = "password";

    private static final String SELECT_ALL_SQL  = "SELECT * FROM Vacancies";
    private static final String DELETE_ALL_SQL  = "DELETE FROM Vacancies";
    private static final String SELECT_CONTAINING_TITLE_SQL  = "SELECT * FROM Vacancies WHERE title LIKE '%?%'";
    private static final String SELECT_CONTAINING_DESCRIPTION_SQL  = "SELECT * FROM Vacancies WHERE description LIKE '%?%'";
    private static final String SELECT_CONTAINING_TITLE_OR_DESCRIPTION_SQL  = "SELECT * FROM Vacancies WHERE title LIKE '%?%' OR description LIKE '%?%'";
    //TODO refactor this
    private static final String INSERT_VACANCY_SQL = "INSERT INTO Vacancies VALUES ('?1', '?2', '?3', '?4', '?5', '?6', ?7, '?8')";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver");
        }
    }

    @Override
    public List<Vacancy> selectAll() {
        return selectContaining(null, false, false);
    }

    @Override
    public List<Vacancy> selectContaining(String query, boolean inTitle, boolean inDescription) {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = null;
        //Select all if inTitle == false and inDescription == false
        if (!inTitle && !inDescription) sql = SELECT_ALL_SQL;
        if (inTitle && !inDescription) sql = SELECT_CONTAINING_TITLE_SQL.replaceAll("\\?", query);
        if (!inTitle && inDescription) sql = SELECT_CONTAINING_DESCRIPTION_SQL.replaceAll("\\?", query);
        if (inTitle && inDescription) sql = SELECT_CONTAINING_TITLE_OR_DESCRIPTION_SQL.replaceAll("\\?", query);

        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vacancy vacancy = new Vacancy();
                vacancy.setCity(rs.getString("city"));
                vacancy.setCompanyName(rs.getString("companyName"));
                vacancy.setSalary(rs.getString("salary"));
                vacancy.setSiteName(rs.getString("siteName"));
                vacancy.setTitle(rs.getString("title"));
                vacancy.setUrl(rs.getString("url"));
                vacancies.add(vacancy);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return vacancies;
    }

    @Override
    public void delete(int id) {}

    @Override
    public void deleteAll() {
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(DELETE_ALL_SQL);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    @Override
    public void add(Vacancy vacancy) {
        String sql = INSERT_VACANCY_SQL.replaceAll("\\?1", vacancy.getTitle());
        sql = sql.replaceAll("\\?2", vacancy.getUrl());
        sql = sql.replaceAll("\\?3", vacancy.getSiteName());
        sql = sql.replaceAll("\\?4", vacancy.getCity());
        sql = sql.replaceAll("\\?5", vacancy.getCompanyName());
        sql = sql.replaceAll("\\?6", vacancy.getSalary());
        sql = sql.replaceAll("\\?7", "0");
        sql = sql.replaceAll("\\?8", "");
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    @Override
    public void addAll(List<Vacancy> vacancies) {
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            for (Vacancy vacancy : vacancies) {
                String sql = INSERT_VACANCY_SQL.replaceAll("\\?1", vacancy.getTitle());
                sql = sql.replaceAll("\\?2", vacancy.getUrl());
                sql = sql.replaceAll("\\?3", vacancy.getSiteName());
                sql = sql.replaceAll("\\?4", vacancy.getCity());
                sql = sql.replaceAll("\\?5", vacancy.getCompanyName());
                sql = sql.replaceAll("\\?6", vacancy.getSalary());
                sql = sql.replaceAll("\\?7", "0");
                sql = sql.replaceAll("\\?8", "");
                stmt.executeUpdate(sql);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }
}

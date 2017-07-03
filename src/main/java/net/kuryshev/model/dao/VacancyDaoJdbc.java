package net.kuryshev.model.dao;


import net.kuryshev.model.SearchParams;
import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import static net.kuryshev.Utils.ClassUtils.getClassName;


public class VacancyDaoJdbc implements VacancyDao {

    private static Logger logger = Logger.getLogger(getClassName());

    //TODO: move settings to property file
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String JDBC_URL = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private static final String user = "root";
    private static final String password = "password";

    private static final String SELECT_ALL_SQL  = "SELECT * FROM Vacancies";
    private static final String DELETE_ALL_SQL  = "DELETE FROM Vacancies";
    private static final String SELECT_CONTAINING_TITLE_SQL  = "SELECT * FROM Vacancies WHERE title LIKE '%?%'";
    private static final String SELECT_CONTAINING_DESCRIPTION_SQL  = "SELECT * FROM Vacancies WHERE description LIKE '%?%'";
    private static final String SELECT_CONTAINING_TITLE_OR_DESCRIPTION_SQL  = "SELECT * FROM Vacancies WHERE title LIKE '%?%' OR description LIKE '%?%'";
    private static final String SELECT_COMPANY_BY_NAME_SQL = "SELECT * FROM Companies WHERE name = '?'";
    //TODO: refactor this
    private static final String INSERT_VACANCY_SQL = "INSERT INTO Vacancies (title, description, url, site_name, city, company, salary, rating) VALUES ('?1', '?2', '?3', '?4', '?5', '?6', '?7', ?8)";
    private static final String INSERT_COMPANY_SQL = "INSERT INTO Companies (name, url, rating, reviews_url) VALUES ('?1', '?2', ?3, '?4')";

    private static Connection con;
    private static Statement stmt, stmtCompany;
    private static ResultSet rs, rsCompany;

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver");
        }
    }

    @Override
    public List<Vacancy> selectAll() {
        return selectContaining(null, SearchParams.SELECT_ALL);
    }

    @Override
    public List<Vacancy> selectContaining(String query, SearchParams params) {
        String sql = null;

        if (params == SearchParams.SELECT_ALL)
            sql = SELECT_ALL_SQL;
        if (params == SearchParams.SEARCH_IN_TITLE)
            sql = SELECT_CONTAINING_TITLE_SQL.replaceAll("\\?", query);
        if (params == SearchParams.SEARCH_IN_DESCRIPTION)
            sql = SELECT_CONTAINING_DESCRIPTION_SQL.replaceAll("\\?", query);
        if (params == SearchParams.SEARCH_IN_TITLE_AND_DESCRIPTION)
            sql = SELECT_CONTAINING_TITLE_OR_DESCRIPTION_SQL.replaceAll("\\?", query);

        return selectVacancies(sql);
    }

    private List<Vacancy> selectVacancies(String sql) {
        List<Vacancy> vacancies = new ArrayList<>();
        Map<String, Company> companyMap = new HashMap<>();

        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            stmtCompany = con.createStatement();

            while (rs.next()) {
                Vacancy vacancy = getVacancyFromResultSet();

                //getting company info
                String companyName = rs.getString("company");
                Company company;
                if ((company = companyMap.get(companyName)) == null) {
                    String sqlCompany = SELECT_COMPANY_BY_NAME_SQL.replaceAll("\\?", companyName);
                    rsCompany = stmtCompany.executeQuery(sqlCompany);
                    if (rsCompany.next()) {
                        company = new Company();
                        company.setName(rsCompany.getString("name"));
                        company.setUrl(rsCompany.getString("url"));
                        company.setRewiewsUrl(rsCompany.getString("reviews_url"));
                        company.setRating(rsCompany.getDouble("rating"));
                        companyMap.put(companyName, company);
                    }
                }
                vacancy.setCompany(company);
                vacancies.add(vacancy);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { logger.error(se.getMessage()); }
            try { stmt.close(); } catch(SQLException se) { logger.error(se.getMessage()); }
            try { stmtCompany.close(); } catch(SQLException se) { logger.error(se.getMessage()); }
            try { rs.close(); } catch(SQLException se) { logger.error(se.getMessage()); }
            try { rsCompany.close(); } catch(SQLException | NullPointerException se) { logger.error(se.getMessage()); }
        }
        return vacancies;
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        executeSqlUpdate(DELETE_ALL_SQL);
    }

    @Override
    public void add(Vacancy vacancy) {
        String sql = getInsertVacancySql(vacancy);

        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmtCompany = con.createStatement();
            rsCompany = stmtCompany.executeQuery(SELECT_COMPANY_BY_NAME_SQL.replaceAll("\\?", vacancy.getCompany().getName()));
            if (!rsCompany.next()) {
                String sqlCompany = getInsertCompanySql(vacancy);
                stmtCompany.executeUpdate(sqlCompany);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmtCompany.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rsCompany.close(); } catch(SQLException se) { /*can't do anything */ }
        }

        executeSqlUpdate(sql);
    }

    @Override
    public void addAll(List<Vacancy> vacancies) {
        Set<String> companyNameSet = new HashSet<>();
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();
            stmtCompany = con.createStatement();

            for (Vacancy vacancy : vacancies) {
                String sql = getInsertVacancySql(vacancy);
                if (!companyNameSet.contains(vacancy.getCompany().getName())) {
                    rsCompany = stmtCompany.executeQuery(SELECT_COMPANY_BY_NAME_SQL.replaceAll("\\?", vacancy.getCompany().getName()));
                    if (!rsCompany.next()) {
                        String sqlCompany = getInsertCompanySql(vacancy);
                        stmtCompany.executeUpdate(sqlCompany);
                    }
                    companyNameSet.add(vacancy.getCompany().getName());
                }
                stmt.executeUpdate(sql);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    private String getInsertCompanySql(Vacancy vacancy) {
        String sqlCompany = INSERT_COMPANY_SQL.replaceAll("\\?1", vacancy.getCompany().getName());
        sqlCompany = sqlCompany.replaceAll("\\?2", vacancy.getCompany().getUrl());
        sqlCompany = sqlCompany.replaceAll("\\?3", vacancy.getCompany().getRating() + "");
        sqlCompany = sqlCompany.replaceAll("\\?4", vacancy.getCompany().getRewiewsUrl());
        return sqlCompany;
    }

    private String getInsertVacancySql(Vacancy vacancy) {
        String sql = INSERT_VACANCY_SQL.replaceAll("\\?1", vacancy.getTitle());
        sql = sql.replaceAll("\\?2", vacancy.getDescription());
        sql = sql.replaceAll("\\?3", vacancy.getUrl());
        sql = sql.replaceAll("\\?4", vacancy.getSiteName());
        sql = sql.replaceAll("\\?5", vacancy.getCity());
        sql = sql.replaceAll("\\?6", vacancy.getCompany().getName());
        sql = sql.replaceAll("\\?7", vacancy.getSalary());
        sql = sql.replaceAll("\\?8", vacancy.getRating() + "");
        return sql;
    }

    private Vacancy getVacancyFromResultSet() throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setCity(rs.getString("city"));
        vacancy.setSalary(rs.getString("salary"));
        vacancy.setSiteName(rs.getString("site_name"));
        vacancy.setTitle(rs.getString("title"));
        vacancy.setUrl(rs.getString("url"));
        vacancy.setRating(rs.getDouble("rating"));
        vacancy.setDescription(rs.getString("description"));
        return vacancy;
    }

    private void executeSqlUpdate(String sql) {
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
}

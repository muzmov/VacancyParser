package net.kuryshev.model.dao;


import net.kuryshev.model.SearchParams;
import net.kuryshev.model.dao.sql.*;
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
        String sql = getSelectSql(params, query);
        return selectVacancies(sql);
    }

    private String getSelectSql(SearchParams params, String query) {
        String sql = "";
        if (params == SearchParams.SELECT_ALL) {
            String[] columns = {};
            String[] filters = {};
            Sql selectSql = new SelectSql("Vacancies", columns, filters);
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_TITLE) {
            String[] columns = {"title"};
            String[] filters = {"%" + query + "%"};
            Sql selectSql = new SelectSql("Vacancies", columns, filters);
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_DESCRIPTION){
            String[] columns = {"description"};
            String[] filters = {"%" + query + "%"};
            Sql selectSql = new SelectSql("Vacancies", columns, filters);
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_TITLE_AND_DESCRIPTION){
            String[] columns = {"title", "description"};
            String[] filters = {"%" + query + "%", "%" + query + "%"};
            Sql selectSql = new SelectSql("Vacancies", columns, filters);
            sql = selectSql.generate();
        }
        return sql;
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

                String companyName = rs.getString("company");
                Company company;
                if ((company = companyMap.get(companyName)) == null) {
                    String[] columns = {"name"};
                    String[] filters = {companyName};
                    Sql selectSql = new SelectSql("Companies", columns, filters);
                    String sqlCompany = selectSql.generate();
                    rsCompany = stmtCompany.executeQuery(sqlCompany);
                    if (rsCompany.next()) {
                        company = getCompanyFromResultSet();
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
        Sql updatesSql = new SetSafeUpdatesSql();
        String sql = updatesSql.generate();
        executeSqlUpdate(sql);
        Sql deleteSql = new DeleteSql("Vacancies");
        sql = deleteSql.generate();
        executeSqlUpdate(sql);
    }

    @Override
    public void add(Vacancy vacancy) {
        String sql = getInsertVacancySql(vacancy);
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmtCompany = con.createStatement();
            String[] columns = {"name"};
            String[] filters = {vacancy.getCompany().getName()};
            Sql selectSql = new SelectSql("Companies", columns, filters);
            String sqlCompany = selectSql.generate();
            rsCompany = stmtCompany.executeQuery(sqlCompany);
            if (!rsCompany.next()) {
                String sqlUpdateCompany = getInsertCompanySql(vacancy);
                stmtCompany.executeUpdate(sqlUpdateCompany);
            }
        } catch (SQLException sqlEx) {
            logger.error("SQL exception occured in addAll: " + sqlEx.getMessage());
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
        logger.debug("Adding a bunch (" + vacancies.size() + ") of vacancies to DB");

        int addedVacanciesCounter = 0;
        try {
            con = DriverManager.getConnection(JDBC_URL, user, password);
            stmt = con.createStatement();

            for (Vacancy vacancy : vacancies) {
                try {
                    if (!companyNameSet.contains(vacancy.getCompany().getName())) {
                        String[] columns = {"name"};
                        String[] filters = {vacancy.getCompany().getName()};
                        Sql selectSql = new SelectSql("Companies", columns, filters);
                        String sqlCompany = selectSql.generate();
                        rsCompany = stmt.executeQuery(sqlCompany);
                        if (!rsCompany.next()) {
                            String sqlInsertCompany = getInsertCompanySql(vacancy);
                            stmt.executeUpdate(sqlInsertCompany);
                        }
                        rsCompany.close();
                        companyNameSet.add(vacancy.getCompany().getName());
                    }
                    String sql = getInsertVacancySql(vacancy);
                    stmt.executeUpdate(sql);
                    ++addedVacanciesCounter;
                } catch (Exception e) {
                    logger.error("Exception while trying to add vacancy to database: " + e.getMessage() + ". On vacancy: " + vacancy);
                }
            }
        } catch (SQLException sqlEx) {
            logger.error("Major SQL exception occured in addAll: " + sqlEx.getMessage());
        } finally {
            try { con.close(); } catch(SQLException se) { logger.error("Can not close connection. Error: " + se.getMessage());}
            try { stmt.close(); } catch(SQLException se) { logger.error("Can not close statement. Error: " + se.getMessage()); }
        }
        logger.trace(addedVacanciesCounter + " vacancies added");
    }

    private String getInsertCompanySql(Vacancy vacancy) {
        Company company = vacancy.getCompany();
        String[] values = {company.getName(), company.getUrl(), company.getRating() + "", company.getRewiewsUrl()};
        Sql insertSql = new InsertSql("Companies", values);
        return insertSql.generate();
    }

    private String getInsertVacancySql(Vacancy vacancy) {
        String[] values = {vacancy.getTitle(), vacancy.getDescription(), vacancy.getUrl(), vacancy.getSiteName(),
                vacancy.getCity(), vacancy.getCompany().getName(), vacancy.getSalary(), vacancy.getRating() + ""};
        Sql insertSql = new InsertSql("Vacancies", values);
        return insertSql.generate();
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

    private Company getCompanyFromResultSet() throws SQLException {
        Company company;
        company = new Company();
        company.setName(rsCompany.getString("name"));
        company.setUrl(rsCompany.getString("url"));
        company.setRewiewsUrl(rsCompany.getString("reviews_url"));
        company.setRating(rsCompany.getDouble("rating"));
        return company;
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

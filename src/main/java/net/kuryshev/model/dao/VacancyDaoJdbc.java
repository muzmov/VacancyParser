package net.kuryshev.model.dao;


import net.kuryshev.model.SearchParams;
import net.kuryshev.model.dao.sql.*;
import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static net.kuryshev.Utils.ClassUtils.getClassName;


public class VacancyDaoJdbc implements VacancyDao {

    private static Logger logger = Logger.getLogger(getClassName());

    private String driverClassName = "com.mysql.jdbc.Driver";
    private String jdbcUrl = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private String user = "root";
    private String password = "password";

    private CompanyDao companyDao;

    private Map<String, Company> companyMap = new HashMap<>();

    public VacancyDaoJdbc() {
        companyDao = new CompanyDaoJdbc();
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver", e);
        }
    }

    public VacancyDaoJdbc(String propertiesPath) throws IllegalArgumentException {
        Properties props = new Properties();
        try {
            props.load(new FileReader(propertiesPath));
        } catch (IOException e) {
            logger.error("Can't open properties file for dao." + e.getMessage());
            throw new IllegalArgumentException();
        }
        driverClassName = props.getProperty("DRIVER_CLASS_NAME");
        jdbcUrl = props.getProperty("JDBC_URL");
        user = props.getProperty("USER");
        password = props.getProperty("PASSWORD");
        if (driverClassName == null || jdbcUrl == null || user == null || password == null) {
            logger.error("Not enough info in property file for dao.");
            throw new IllegalArgumentException();
        }
        companyDao = new CompanyDaoJdbc(propertiesPath);
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver", e);
        }
    }

    @Override
    public List<Vacancy> selectAll() {
        return selectContaining(null, SearchParams.SELECT_ALL);
    }

    @Override
    public List<Vacancy> selectContaining(String query, SearchParams params) {
        String sql = getSelectSql(params, query);
        return executeVacanciesSelect(sql);
    }

    private String getSelectSql(SearchParams params, String query) {
        String sql = "";
        if (params == SearchParams.SELECT_ALL) {
            Sql selectSql = new SelectSql("Vacancies");
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_TITLE) {
            String[] columns = {"title"};
            String[] filters = {"%" + query + "%"};
            SelectSql selectSql = new SelectSql("Vacancies");
            selectSql.setFilters(columns, filters, "OR");
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_DESCRIPTION){
            String[] columns = {"description"};
            String[] filters = {"%" + query + "%"};
            SelectSql selectSql = new SelectSql("Vacancies");
            selectSql.setFilters(columns, filters, "OR");
            sql = selectSql.generate();
        }
        if (params == SearchParams.SEARCH_IN_TITLE_AND_DESCRIPTION){
            String[] columns = {"title", "description"};
            String[] filters = {"%" + query + "%", "%" + query + "%"};
            SelectSql selectSql = new SelectSql("Vacancies");
            selectSql.setFilters(columns, filters, "OR");
            sql = selectSql.generate();
        }
        return sql;
    }

    private List<Vacancy> executeVacanciesSelect(String sql) {
        List<Vacancy> vacancies = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement();
             Statement stmtCompany = con.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Vacancy vacancy = getVacancyFromResultSet(rs);
                vacancies.add(vacancy);
            }
        } catch (SQLException sqlEx) {
            logger.error("Error during select.", sqlEx);
        }
        return vacancies;
    }

    private Vacancy getVacancyFromResultSet(ResultSet rs) throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setCity(rs.getString("city"));
        vacancy.setSalary(rs.getString("salary"));
        vacancy.setSiteName(rs.getString("site_name"));
        vacancy.setTitle(rs.getString("title"));
        vacancy.setUrl(rs.getString("url"));
        vacancy.setRating(rs.getDouble("rating"));
        vacancy.setDescription(rs.getString("description"));

        String companyName = rs.getString("company");
        Company company;
        if ((company = companyMap.get(companyName)) == null) {
            company = companyDao.getCompanyByName(companyName);
            if (company == null) company = new Company();
            companyMap.put(companyName, company);
        }
        vacancy.setCompany(company);

        return vacancy;
    }

    @Override
    public void deleteAll() {
        Sql updatesSql = new SetSafeUpdatesSql();
        String sql = updatesSql.generate();
        executeSqlUpdate(sql);
        DeleteSql deleteSql = new DeleteSql("Vacancies");
        sql = deleteSql.generate();
        executeSqlUpdate(sql);
        deleteSql = new DeleteSql("Companies");
        sql = deleteSql.generate();
        executeSqlUpdate(sql);
        logger.info("All vacancies deleted");
    }

    private void executeSqlUpdate(String sql) {
        try (
                Connection con = DriverManager.getConnection(jdbcUrl, user, password);
                Statement stmt = con.createStatement();)
        {
            stmt.executeUpdate(sql);
        } catch (SQLException sqlEx) {
            logger.error("Exception during sql update.", sqlEx);
        }
    }

    @Override
    public void add(Vacancy vacancy) {
        List<Vacancy> vacancyList = new ArrayList<>();
        vacancyList.add(vacancy);
        addAll(vacancyList);
    }

    @Override
    public void addAll(List<Vacancy> vacancies) {
        Set<String> companyNameSet = new HashSet<>();
        logger.debug("Adding a bunch (" + vacancies.size() + ") of vacancies to DB");

        int addedVacanciesCounter = 0;
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement();)
        {
            con.setAutoCommit(false);
            for (Vacancy vacancy : vacancies) {
                try {
                    String companyName = vacancy.getCompany().getName();
                    if (!companyNameSet.contains(companyName)) {
                        Company company = companyDao.getCompanyByName(companyName);
                        if (company == null) companyDao.add(vacancy.getCompany());
                        companyNameSet.add(vacancy.getCompany().getName());
                    }
                    String sql = getInsertVacancySql(vacancy);
                    stmt.executeUpdate(sql);
                    ++addedVacanciesCounter;
                } catch (Exception e) {
                    logger.warn("This vacancy was not added to database:" + vacancy, e);
                }
            }
            con.commit();
        } catch (SQLException sqlEx) {
            logger.error("Major SQL exception occured in addAll.", sqlEx);
        }
        logger.trace(addedVacanciesCounter + " vacancies added");
    }

    private String getInsertVacancySql(Vacancy vacancy) {
        String[] values = {vacancy.getTitle(), vacancy.getDescription(), vacancy.getUrl(), vacancy.getSiteName(),
                vacancy.getCity(), vacancy.getCompany().getName(), vacancy.getSalary(), vacancy.getRating() + ""};
        Sql insertSql = new InsertSql("Vacancies", values);
        return insertSql.generate();
    }
}

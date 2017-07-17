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
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static final String INSERT_VACANCY_SQL =  "INSERT INTO Vacancies (title, description, url, site_name, city, " +
            "company, salary, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_COMPANY_SQL = "INSERT INTO Companies (name, url, rating, reviews_url) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SELECT_COMPANY_SQL = "SELECT * FROM Companies WHERE name = ?";

    private ResultSet rs, rsCompany;

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
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
        Map<String, Company> companyMap = new HashMap<>();

        try (Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement stmt = con.createStatement();
            Statement stmtCompany = con.createStatement())
        {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vacancy vacancy = getVacancyFromResultSet(rs);

                String companyName = rs.getString("company");
                Company company;
                if ((company = companyMap.get(companyName)) == null) {
                    String[] columns = {"name"};
                    String[] filters = {companyName};
                    SelectSql selectSql = new SelectSql("Companies");
                    selectSql.setFilters(columns, filters, "OR");
                    String sqlCompany = selectSql.generate();
                    ResultSet rsCompany = stmtCompany.executeQuery(sqlCompany);
                    if (rsCompany.next()) {
                        company = getCompanyFromResultSet(rsCompany);
                        companyMap.put(companyName, company);
                    }
                }
                vacancy.setCompany(company);
                vacancies.add(vacancy);
            }
        } catch (SQLException sqlEx) {
            logger.error("Error during select.", sqlEx);
        }
        return vacancies;
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
        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement stmtSelectCompany = con.prepareStatement(SELECT_COMPANY_SQL);
                PreparedStatement stmtInsertVacancy = con.prepareStatement(INSERT_VACANCY_SQL);
                PreparedStatement stmtInsertCompany = con.prepareStatement(INSERT_COMPANY_SQL))
        {
            for (Vacancy vacancy : vacancies) {
                try {
                    if (!companyNameSet.contains(vacancy.getCompany().getName())) {
                        stmtSelectCompany.setString(1, vacancy.getCompany().getName());
                        ResultSet rsCompany = stmtSelectCompany.executeQuery();
                        if (!rsCompany.next()) {
                            setInsertCompanyStmt(stmtInsertCompany, vacancy.getCompany());
                            stmtInsertCompany.executeUpdate();
                        }
                        rsCompany.close();
                        companyNameSet.add(vacancy.getCompany().getName());
                    }
                    setInsertVacancyStmt(stmtInsertVacancy, vacancy);
                    stmtInsertVacancy.executeUpdate();
                    ++addedVacanciesCounter;
                } catch (Exception e) {
                    logger.warn("This vacancy was not added to database:" + vacancy + ". " + e.getMessage());
                }
            }
        } catch (SQLException sqlEx) {
            logger.error("Major SQL exception occured in addAll.", sqlEx);
        }
        logger.trace(addedVacanciesCounter + " vacancies added");
    }

    private void setInsertCompanyStmt(PreparedStatement stmt, Company company) throws SQLException {
        stmt.setString(1, company.getName());
        stmt.setString(2, company.getUrl());
        stmt.setDouble(3, company.getRating());
        stmt.setString(4, company.getRewiewsUrl());
    }

    private void setInsertVacancyStmt(PreparedStatement stmt, Vacancy vacancy) throws SQLException {
        stmt.setString(1, vacancy.getTitle());
        stmt.setString(2, vacancy.getDescription());
        stmt.setString(3, vacancy.getUrl());
        stmt.setString(4, vacancy.getSiteName());
        stmt.setString(5, vacancy.getCity());
        stmt.setString(6, vacancy.getCompany().getName());
        stmt.setString(7, vacancy.getSalary());
        stmt.setDouble(8, vacancy.getRating());

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
        return vacancy;
    }

    private Company getCompanyFromResultSet(ResultSet rsCompany) throws SQLException {
        Company company;
        company = new Company();
        company.setName(rsCompany.getString("name"));
        company.setUrl(rsCompany.getString("url"));
        company.setRewiewsUrl(rsCompany.getString("reviews_url"));
        company.setRating(rsCompany.getDouble("rating"));
        return company;
    }

    private void executeSqlUpdate(String sql) {
        try (
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement stmt = con.createStatement();)
        {
            stmt.executeUpdate(sql);
        } catch (SQLException sqlEx) {
            logger.error("Exception during sql update.", sqlEx);
        }
    }
}

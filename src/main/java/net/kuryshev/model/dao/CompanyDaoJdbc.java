package net.kuryshev.model.dao;

import net.kuryshev.model.dao.sql.*;
import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class CompanyDaoJdbc implements CompanyDao {
    private static Logger logger = Logger.getLogger(getClassName());

    private String driverClassName = "com.mysql.jdbc.Driver";
    private String jdbcUrl = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private String user = "root";
    private String password = "password";


    public CompanyDaoJdbc() {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver", e);
        }

    }

    public void setProperties(String propertiesPath) throws IllegalArgumentException {
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
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver", e);
        }
    }

    @Override
    public List<Company> selectAll() {
        List<Company> companies = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement())
        {
            SelectSql selectSql = new SelectSql("Companies");
            String sql = selectSql.generate();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company company = getCompanyFromResultSet(rs);
                companies.add(company);
            }
        } catch (SQLException sqlEx) {
            logger.error("Error during select.", sqlEx);
        }
        return companies;
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

    @Override
    public void add(Company company) {
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        addAll(companies);
    }

    @Override
    public void addAll(List<Company> companies) {
        Set<String> companyNameSet = new HashSet<>();
        logger.debug("Adding a bunch (" + companies.size() + ") of companies to DB");

        int addedVacanciesCounter = 0;
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement();)
        {
            con.setAutoCommit(false);
            for (Company company : companies) {
                try {
                    String sql = getInsertCompanySql(company);
                    stmt.addBatch(sql);
                    ++addedVacanciesCounter;
                } catch (Exception e) {
                    logger.warn("Comething is wrong with this company:" + company, e);
                }
            }
            stmt.executeBatch();
            con.commit();
        } catch (SQLException sqlEx) {
            logger.error("Major SQL exception occured in addAll.", sqlEx);
        }
        logger.trace(addedVacanciesCounter + " companies added");
    }

    private String getInsertCompanySql(Company company) {
        String[] values = {company.getName(), company.getUrl(), company.getRating() + "", company.getRewiewsUrl()};
        Sql insertSql = new InsertSql("Companies", values);
        return insertSql.generate();
    }

    @Override
    public void deleteAll() {
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement())
        {
            Sql sql = new SetSafeUpdatesSql();
            stmt.executeUpdate(sql.generate());
            sql = new DeleteSql("Companies");
            stmt.executeUpdate(sql.generate());
        }
        catch (SQLException sqlEx) {
            logger.error("Error during delete.", sqlEx);
        }
    }

    @Override
    public void update(Company company) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Company getCompanyByName(String companyName) {
        Company company = null;
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = con.createStatement())
        {
            String sql = getSelectSql(companyName);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) company = getCompanyFromResultSet(rs);
        } catch (SQLException sqlEx) {
            logger.error("Error during select.", sqlEx);
        }
        return company;
    }

    private String getSelectSql(String companyName) {
        SelectSql selectSql = new SelectSql("Companies");
        String[] columns = {"name"};
        String[] filters = {companyName};
        selectSql.setFilters(columns, filters, "OR");
        return selectSql.generate();
    }
}

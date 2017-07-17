package net.kuryshev.model.dao;

import net.kuryshev.model.dao.sql.*;
import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class CompanyDaoJdbc implements CompanyDao {
    private static Logger logger = Logger.getLogger(getClassName());

    //TODO: move settings to property file
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String JDBC_URL = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "password";


    @Override
    public List<Company> selectAll() {
        List<Company> companies = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
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
        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                Statement stmt = con.createStatement();)
        {
            con.setAutoCommit(false);
            for (Company company : companies) {
                try {
                    String sql = getInsertCompanySql(company);
                    stmt.executeUpdate(sql);
                    ++addedVacanciesCounter;
                } catch (Exception e) {
                    logger.warn("This company was not added to database:" + company, e);
                }
            }
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
        try (Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
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
}

package net.kuryshev.model.dao;

import net.kuryshev.model.dao.sql.DeleteSql;
import net.kuryshev.model.dao.sql.SelectSql;
import net.kuryshev.model.dao.sql.SetSafeUpdatesSql;
import net.kuryshev.model.dao.sql.Sql;
import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public void addAll(List<Company> company) {

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

    }
}

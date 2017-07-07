package net.kuryshev.model.dao;

import net.kuryshev.model.dao.sql.DeleteSql;
import net.kuryshev.model.dao.sql.InsertSql;
import net.kuryshev.model.dao.sql.SelectSql;
import net.kuryshev.model.dao.sql.Sql;
import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.UserRole;
import org.apache.log4j.Logger;

import java.sql.*;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class UserDaoJdbc implements UserDao {

    private static Logger logger = Logger.getLogger(getClassName());

    //TODO: move settings to property file
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String JDBC_URL = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find MYSQL driver", e);
        }
    }

    @Override
    public UserRole getRole(User user) {
        try {
            con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            stmt = con.createStatement();
            String[] columns = {"login", "password"};
            String[] filters = {user.getLogin(), user.getPassword()};
            SelectSql selectSql = new SelectSql("Users");
            selectSql.setFilters(columns, filters, "AND");
            String sql = selectSql.generate();
            rs = stmt.executeQuery(sql);
            String role = null;
            if (rs.next())
                role = rs.getString("role");
            if (role == null) return UserRole.NONE;
            if (role.equals("ADMIN")) return UserRole.ADMIN;
            if (role.equals("USER")) return UserRole.USER;
            return UserRole.NONE;
        }
        catch (SQLException e) {
            logger.error("Major SQL Exception", e);
            return UserRole.NONE;
        }
        finally {
            try { con.close();} catch (Exception e) { /*NOP*/ }
            try { stmt.close();} catch (Exception e) { /*NOP*/ }
            try { rs.close();} catch (Exception e) {/*NOP*/ }
        }
    }

    @Override
    public void addUser(User user) {
        try {
            con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            stmt = con.createStatement();
            String[] values = {user.getLogin(), user.getPassword(), user.getRole().toString()};
            Sql insertSql = new InsertSql("Users", values);
            String sql = insertSql.generate();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            logger.error("Major SQL Exception", e);
        }
        finally {
            try { con.close();} catch (Exception e) { /*NOP*/ }
            try { stmt.close();} catch (Exception e) { /*NOP*/ }
        }
    }

    @Override
    public void deleteUser(String login) {
        try {
            con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            stmt = con.createStatement();
            String[] filters = {login};
            String[] columns = {"login"};
            DeleteSql deleteSql = new DeleteSql("Users");
            deleteSql.setFilters(columns, filters, "OR");
            String sql = deleteSql.generate();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            logger.error("Major SQL Exception", e);
        }
        finally {
            try { con.close();} catch (Exception e) { /*NOP*/ }
            try { stmt.close();} catch (Exception e) { /*NOP*/ }
        }
    }
}

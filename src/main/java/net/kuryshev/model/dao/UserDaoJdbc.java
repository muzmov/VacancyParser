package net.kuryshev.model.dao;

import net.kuryshev.model.dao.sql.DeleteSql;
import net.kuryshev.model.dao.sql.InsertSql;
import net.kuryshev.model.dao.sql.SelectSql;
import net.kuryshev.model.dao.sql.Sql;
import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.UserRole;
import org.apache.log4j.Logger;

import java.sql.*;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class UserDaoJdbc extends DaoJdbc implements UserDao {

    private static Logger logger = Logger.getLogger(getClassName());

    @Override
    public UserRole getRole(User user) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, this.user, password);
            Statement stmt = con.createStatement())
        {
            String[] columns = {"login", "password"};
            String[] filters = {user.getLogin(), user.getPassword()};
            SelectSql selectSql = new SelectSql("Users");
            selectSql.setFilters(columns, filters, "AND");
            String sql = selectSql.generate();
            ResultSet rs = stmt.executeQuery(sql);
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
    }

    @Override
    public void addUser(User user) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, this.user, password);
             Statement stmt = con.createStatement())
        {
            String[] values = {user.getLogin(), user.getPassword(), user.getRole().toString()};
            Sql insertSql = new InsertSql("Users", values);
            String sql = insertSql.generate();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            logger.error("Major SQL Exception", e);
        }
    }

    @Override
    public void deleteUser(String login) {
        try (Connection con = DriverManager.getConnection(jdbcUrl, this.user, password);
             Statement stmt = con.createStatement())
        {
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
    }
}

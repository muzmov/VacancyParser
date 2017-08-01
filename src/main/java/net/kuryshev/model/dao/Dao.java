package net.kuryshev.model.dao;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static net.kuryshev.utils.ClassUtils.getClassName;

public abstract class Dao {
    private static Logger logger = Logger.getLogger(getClassName());

    String driverClassName = "com.mysql.jdbc.Driver";
    String jdbcUrl = "jdbc:mysql://localhost:3306/vacancyparser?useSSL=false";
    String user = "root";
    String password = "password";

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

}

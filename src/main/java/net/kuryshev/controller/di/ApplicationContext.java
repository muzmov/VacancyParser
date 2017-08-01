package net.kuryshev.controller.di;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class ApplicationContext {
    private Logger logger = Logger.getLogger(getClassName());

    private Properties props = new Properties();

    public void init(String propsFilename) throws IOException {
        props.load(new FileReader(propsFilename));
    }

    public Object getBean(String name) {
        Object result = null;
        try {
            String className = props.getProperty(name);
            if (className == null) return null;
            Class clazz = Class.forName(className);
            result = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("Can not found class for injection", e);
        } catch (InstantiationException e) {
            logger.error("Wrong injection class in property file.", e);
        } catch (IllegalAccessException e) {
            logger.error("Can not initialize new injection object. Injection class should have public constructor with no parameters", e);
        }
        return result;
    }
}

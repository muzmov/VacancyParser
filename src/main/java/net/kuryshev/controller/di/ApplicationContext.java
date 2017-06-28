package net.kuryshev.controller.di;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static net.kuryshev.Utils.ClassUtils.getClassName;

/**
 * Created by 1 on 27.06.2017.
 */
public class ApplicationContext {
    Logger logger = Logger.getLogger(getClassName());

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
            e.printStackTrace();
        } catch (InstantiationException e) {
            logger.error("Wrong injection class in property file.");
        } catch (IllegalAccessException e) {
            logger.error("Can not initialize new injection object. Injection class should have public constructor with no parameters");
        }
        return result;
    }
}

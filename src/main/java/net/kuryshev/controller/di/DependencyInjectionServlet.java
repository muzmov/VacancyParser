package net.kuryshev.controller.di;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class DependencyInjectionServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(getClassName());

    private static String PROPERTIES_FILENAME = "webapps/VacancyParser/WEB-INF/classes/injection.properties";

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext appCtx = new ApplicationContext();
        try {
            File file = new File("a.txt");
            System.out.println(file.getAbsolutePath());
            appCtx.init(PROPERTIES_FILENAME);
        } catch (IOException e) {
            logger.error("There is no properties file for injection.", e);
            throw new ServletException("There is no properties file for injection.", e);
        }

        Class clazz = this.getClass();
        while (clazz != DependencyInjectionServlet.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Inject annotation = field.getAnnotation(Inject.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Object fieldValue = appCtx.getBean(annotation.value());
                    if (fieldValue == null) {
                        logger.error("Something is wrong with properties file.");
                    }
                    try {
                        field.set(this, fieldValue);
                    } catch (IllegalAccessException e) {
                        logger.error("Unexpected error in dependency injection.", e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

    }
}

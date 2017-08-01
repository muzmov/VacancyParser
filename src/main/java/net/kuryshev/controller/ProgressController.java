package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.model.TaskProgress;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class ProgressController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String attrName = request.getParameter("attr");
        logger.debug("parse.do attr param = " + attrName);
        TaskProgress progress = (TaskProgress) request.getSession().getAttribute(attrName);
        if (progress != null) {
            logger.debug("Progress not null. Value = " + progress.getProgress());
            if (progress.isDone()) request.getSession().removeAttribute("Progress");
            response.getWriter().write(progress.getProgress());
        }
        else {
            logger.debug("Progress null");
        }
    }
}

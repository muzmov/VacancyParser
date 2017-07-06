package net.kuryshev.model;

import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.Strategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class VacancyParserImpl implements VacancyParser {
    private Logger logger = Logger.getLogger(getClassName());

    //TODO make dynamic
    private VacancyDao dao = new VacancyDaoJdbc();
    private Strategy[] strategies;

    public VacancyParserImpl(Strategy... strategies) {
        if (strategies == null || strategies.length == 0) throw new IllegalArgumentException();
        this.strategies = strategies;
    }

    @Override
    public List<Vacancy> searchContaining(String query) {
        query = query.replaceAll(" ", "+");
        List<Vacancy> vacancies = new ArrayList<>();
        if (logger.isDebugEnabled()) {
            String strategiesString = "";
            for (Strategy strategy : strategies)
                strategiesString += strategy.getClass().getSimpleName() + ", ";
            logger.debug("Starting to parse vacancies from " + strategiesString);
        }
        long timeStart = System.currentTimeMillis();
        for (Strategy strategy : strategies)
            vacancies.addAll(strategy.getVacancies(query));
        long timeFinish = System.currentTimeMillis();
        logger.info("All vacancies (" + vacancies.size() + ") parsed in " + (timeFinish - timeStart) + "ms");
        timeStart = System.currentTimeMillis();
        dao.addAll(vacancies);
        timeFinish = System.currentTimeMillis();
        logger.info("All vacancies added to database in " + (timeFinish - timeStart) + "ms");
        return vacancies;
    }
}

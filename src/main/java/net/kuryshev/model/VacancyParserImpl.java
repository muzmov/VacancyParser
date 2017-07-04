package net.kuryshev.model;

import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.Strategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class VacancyParserImpl implements VacancyParser {
    private static Logger logger = Logger.getLogger(getClassName());

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
        String strategiesString = "";
        for (Strategy strategy : strategies)
            strategiesString += strategy.getClass().getSimpleName() + ", ";
        logger.trace("Starting to parse vacancies from " + strategiesString);
        Date date1 = new Date();
        for (Strategy strategy : strategies)
            vacancies.addAll(strategy.getVacancies(query));
        Date date2 = new Date();
        logger.trace("All vacancies (" + vacancies.size() + ") parsed in " + (date2.getTime() - date1.getTime()) + "ms");
        date1 = new Date();
        dao.addAll(vacancies);
        date2 = new Date();
        logger.trace("All vacancies added to database in " + (date2.getTime() - date1.getTime()) + "ms");
        return vacancies;
    }
}

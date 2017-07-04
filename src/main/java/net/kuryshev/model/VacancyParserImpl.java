package net.kuryshev.model;

import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.Provider;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

/**
 * Created by 1 on 28.06.2017.
 */
public class VacancyParserImpl implements VacancyParser {
    private static Logger logger = Logger.getLogger(getClassName());

    //TODO make dynamic
    private VacancyDao dao = new VacancyDaoJdbc();
    private Provider[] providers;

    public VacancyParserImpl(Provider... providers) {
        if (providers == null || providers.length == 0) throw new IllegalArgumentException();
        this.providers = providers;
    }

    @Override
    public List<Vacancy> searchContaining(String query) {
        query = query.replaceAll(" ", "+");
        List<Vacancy> vacancies = new ArrayList<>();
        String strategiesString = "";
        for (Provider provider : providers)
            strategiesString += provider.getStrategy().getClass().getSimpleName() + ", ";
        logger.trace("Starting to parse vacancies from " + strategiesString);
        Date date1 = new Date();
        for (Provider provider : providers)
            vacancies.addAll(provider.getVacanciesContaining(query));
        Date date2 = new Date();
        logger.trace("All vacancies (" + vacancies.size() + ") parsed in " + (date2.getTime() - date1.getTime()) + "ms");
        date1 = new Date();
        dao.addAll(vacancies);
        date2 = new Date();
        logger.trace("All vacancies added to database in " + (date2.getTime() - date1.getTime()) + "ms");
        return vacancies;
    }
}

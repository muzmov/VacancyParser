package net.kuryshev.model;

import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.Provider;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class VacancyParserImpl implements VacancyParser {
    private Logger logger = Logger.getLogger(getClassName());

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
        if (logger.isDebugEnabled()) {
            String strategiesString = "";
            for (Provider provider : providers)
                strategiesString += provider.getStrategy().getClass().getSimpleName() + ", ";
            logger.debug("Starting to parse vacancies from " + strategiesString);
        }
        long timeStart = System.currentTimeMillis();
        for (Provider provider : providers)
            vacancies.addAll(provider.getVacancies(query));
        long timeFinish = System.currentTimeMillis();
        logger.info("All vacancies (" + vacancies.size() + ") parsed in " + (timeFinish - timeStart) + "ms");
        timeStart = System.currentTimeMillis();
        dao.addAll(vacancies);
        timeFinish = System.currentTimeMillis();
        logger.info("All vacancies added to database in " + (timeFinish - timeStart) + "ms");
        return vacancies;
    }
}

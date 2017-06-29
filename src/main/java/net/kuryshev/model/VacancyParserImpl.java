package net.kuryshev.model;

import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.strategy.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 28.06.2017.
 */
public class VacancyParserImpl implements VacancyParser {
    //TODO make dynamic
    private VacancyDao dao = new VacancyDaoJdbc();
    private Provider[] providers;

    public VacancyParserImpl(Provider... providers) {
        if (providers == null || providers.length == 0) throw new IllegalArgumentException();
        this.providers = providers;
    }

    public List<Vacancy> selectCity(String city) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers)
            vacancies.addAll(provider.getJavaVacancies(city));
        dao.addAll(vacancies);
        return vacancies;
    }

    @Override
    public List<Vacancy> searchContaining(String query, boolean inTitle, boolean inDescription) {
        return null;
    }
}

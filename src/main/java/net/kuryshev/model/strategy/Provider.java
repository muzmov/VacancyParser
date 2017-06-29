package net.kuryshev.model.strategy;


import net.kuryshev.model.Vacancy;

import java.util.List;

/**
 * Created by 1 on 08.06.2017.
 */
public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public List<Vacancy> getVacanciesContaining(String searchString) {
        return strategy.getVacancies(searchString);
    }
}

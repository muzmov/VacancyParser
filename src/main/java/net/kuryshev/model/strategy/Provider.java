package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Vacancy;

import java.util.List;

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

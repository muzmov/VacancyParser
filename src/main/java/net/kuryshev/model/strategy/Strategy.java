package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getVacancies(String searchString);
}

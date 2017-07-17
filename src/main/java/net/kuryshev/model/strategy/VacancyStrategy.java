package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Vacancy;

import java.util.List;

public interface VacancyStrategy {

    List<Vacancy> getVacancies(String searchString, int initialPage, int jump);

}

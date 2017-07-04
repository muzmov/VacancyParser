package net.kuryshev.model;

import net.kuryshev.model.entity.Vacancy;

import java.util.List;

public interface VacancyParser {
    List<Vacancy> searchContaining(String query);
}

package net.kuryshev.model.dao;

import net.kuryshev.model.SearchParams;
import net.kuryshev.model.entity.Vacancy;

import java.util.List;

public abstract class VacancyDao extends Dao {

    public abstract List<Vacancy> selectAll();

    public abstract List<Vacancy> selectContaining(String query, SearchParams params);

    public abstract void deleteAll();

    public abstract void add(Vacancy vacancy);

    public abstract void addAll(List<Vacancy> vacancies);
}

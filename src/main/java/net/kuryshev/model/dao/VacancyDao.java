package net.kuryshev.model.dao;

import net.kuryshev.model.SearchParams;
import net.kuryshev.model.entity.Vacancy;

import java.util.List;

/**
 * Created by 1 on 25.06.2017.
 */
public interface VacancyDao {
    List<Vacancy> selectAll();

    List<Vacancy> selectContaining(String query, SearchParams params);

    void delete(int id);

    void deleteAll();

    void add(Vacancy vacancy);

    void addAll(List<Vacancy> vacancies);
}

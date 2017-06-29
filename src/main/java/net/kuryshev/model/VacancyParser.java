package net.kuryshev.model;

import java.util.List;

/**
 * Created by 1 on 28.06.2017.
 */
public interface VacancyParser {
    List<Vacancy> searchContaining(String query, boolean inTitle, boolean inDescription);
}

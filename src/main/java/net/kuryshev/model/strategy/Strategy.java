package net.kuryshev.model.strategy;


import net.kuryshev.model.entity.Vacancy;

import java.util.List;

/**
 * Created by 1 on 08.06.2017.
 */
public interface Strategy {
    List<Vacancy> getVacancies(String searchString);
}

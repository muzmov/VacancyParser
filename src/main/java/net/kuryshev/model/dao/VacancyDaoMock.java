package net.kuryshev.model.dao;

import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 25.06.2017.
 */
public class VacancyDaoMock implements VacancyDao {

    public List<Vacancy> selectAll() {
        List<Vacancy> result = new ArrayList<Vacancy>();
        for (int i = 0; i < 10; i++) {
            result.add(new Vacancy("test" + i, "test" + i,"test" + i,"test" + i,"test" + i,"test" + i, 5, new Company()));
        }
        return result;
    }

    @Override
    public List<Vacancy> selectContaining(String query, boolean inTitle, boolean inDescription) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void add(Vacancy vacancy) {

    }

    @Override
    public void addAll(List<Vacancy> vacancies) {

    }
}

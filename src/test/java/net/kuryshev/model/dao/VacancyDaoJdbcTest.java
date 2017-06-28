package net.kuryshev.model.dao;

import net.kuryshev.model.Vacancy;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1 on 27.06.2017.
 */
public class VacancyDaoJdbcTest {
    private static final VacancyDaoJdbc dao = new VacancyDaoJdbc();

    @Test
    public void selectAll_FromEmpty_Test() throws Exception {
        dao.deleteAll();
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void addOne_SelectAll_Test() {
        dao.deleteAll();
        Vacancy vacancy = new Vacancy("test","test","test","test","test","test");
        dao.add(vacancy);
        List<Vacancy> result = dao.selectAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), vacancy);
    }

    @Test
    public void add100_SelectAll_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i, "test" + i, "test" + i, "test" + i, "test" + i);
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectAll();
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 100);
    }

    @Test
    public void addMany_SearchContaining_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i, "test" + i, "test" + i, "test" + i, "test" + i);
            vacancies.add(vacancy);
        }
        dao.addAll(vacancies);
        vacancies = dao.selectContaining("1", true, false);
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 19);
    }

    @Test
    public void addMany_DeleteAll_Test() {
        List<Vacancy> vacancies = new ArrayList<>();
        dao.deleteAll();
        for (int i = 0; i < 100; i++) {
            Vacancy vacancy = new Vacancy("test" + i, "test" + i, "test" + i, "test" + i, "test" + i, "test" + i);
        }
        dao.addAll(vacancies);
        dao.deleteAll();
        vacancies = dao.selectAll();
        Assert.assertNotNull(vacancies);
        Assert.assertEquals(vacancies.size(), 0);
    }

}
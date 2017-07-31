package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class NoComparatorFactory implements ComparatorFactory {

    @Override
    public Comparator<Vacancy> getComparator() {
        return null;
    }
}

package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public interface ComparatorFactory {
    Comparator<Vacancy> getComparator();
}

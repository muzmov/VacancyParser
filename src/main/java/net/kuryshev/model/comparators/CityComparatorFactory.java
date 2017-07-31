package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class CityComparatorFactory implements ComparatorFactory {
    private boolean reverseOrder;

    public CityComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator =  new CityComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    class CityComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return o1.getCity().compareTo(o2.getCity());
        }
    };
}

package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class TitleComparatorFactory implements ComparatorFactory {
    private boolean reverseOrder;

    TitleComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator =  new TitleComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    private static class TitleComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}

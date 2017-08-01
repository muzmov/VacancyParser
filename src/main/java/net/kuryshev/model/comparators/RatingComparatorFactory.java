package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class RatingComparatorFactory implements ComparatorFactory{
    private boolean reverseOrder;

    RatingComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator = new RatingComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    static class RatingComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return (int) (o1.getCompany().getRating() * 100  - o2.getCompany().getRating() * 100);
        }
    }
}

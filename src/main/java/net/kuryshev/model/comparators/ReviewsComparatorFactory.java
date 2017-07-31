package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class ReviewsComparatorFactory implements ComparatorFactory{
    private boolean reverseOrder;

    public ReviewsComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator = new ReviewsComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    class ReviewsComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return o1.getCompany().getRewiewsUrl().compareTo(o2.getCompany().getRewiewsUrl());
        }
    };
}

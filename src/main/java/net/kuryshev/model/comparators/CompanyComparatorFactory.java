package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class CompanyComparatorFactory implements ComparatorFactory {
    private boolean reverseOrder;

    public CompanyComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator = new CompanyComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    class CompanyComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return o1.getCompany().getName().compareTo(o2.getCompany().getName());
        }
    };
}

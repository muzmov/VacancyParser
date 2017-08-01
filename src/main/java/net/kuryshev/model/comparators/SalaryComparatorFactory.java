package net.kuryshev.model.comparators;

import net.kuryshev.model.entity.Vacancy;

import java.util.Comparator;

public class SalaryComparatorFactory implements ComparatorFactory {
    private boolean reverseOrder;

    SalaryComparatorFactory(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public Comparator<Vacancy> getComparator() {
        Comparator<Vacancy> comparator = new SalaryComparator();
        if (reverseOrder) return comparator.reversed();
        else return comparator;
    }

    static class SalaryComparator implements Comparator<Vacancy> {
        @Override
        public int compare(Vacancy o1, Vacancy o2) {
            return Integer.compare(getAvgSalary(o1.getSalary()), getAvgSalary(o2.getSalary()));
        }

        int getAvgSalary(String salaryString) {
            if (salaryString.isEmpty()) return 0;
            salaryString = salaryString.replaceAll("\\s", "");
            salaryString = salaryString.replaceAll("\\u00A0", "");
            String temp = "";
            int i = 0;
            while (i < salaryString.length()) {
                char c = salaryString.charAt(i++);
                if (c <= '9' && c >= '0') {
                    while (c <= '9' && c >= '0' && i < salaryString.length()) {
                        temp += c;
                        c = salaryString.charAt(i++);
                    }
                    break;
                }
            }
            if (temp.isEmpty()) return 0;
            else return Integer.parseInt(temp);
        }
    }
}

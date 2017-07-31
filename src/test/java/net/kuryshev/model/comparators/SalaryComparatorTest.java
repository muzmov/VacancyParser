package net.kuryshev.model.comparators;

import org.junit.Assert;
import org.junit.Test;

public class SalaryComparatorTest {
    @Test
    public void getAvgSalaryTest() {
        SalaryComparator comparator = new SalaryComparator();
        Assert.assertEquals(250000, comparator.getAvgSalary("250 000-1 000 000 KZT"));
        Assert.assertEquals(40000, comparator.getAvgSalary("От 40 000 до 80 000 руб. "));
        Assert.assertEquals(230000, comparator.getAvgSalary("От 230 000 руб. "));
        Assert.assertEquals(1200, comparator.getAvgSalary("1 200-2 500 USD "));
        Assert.assertEquals(0, comparator.getAvgSalary("USD "));
    }

}
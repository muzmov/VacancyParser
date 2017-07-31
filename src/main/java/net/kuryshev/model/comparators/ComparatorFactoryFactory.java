package net.kuryshev.model.comparators;

public class ComparatorFactoryFactory {
    public static ComparatorFactory getFactory(String type) {
        if (type == null) return new NoComparatorFactory();
        switch (type) {
            case "title": return new TitleComparatorFactory(false);
            case "-title": return new TitleComparatorFactory(true);
            case "city": return new CityComparatorFactory(false);
            case "-city": return new CityComparatorFactory(true);
            case "company": return new CompanyComparatorFactory(false);
            case "-company": return new CompanyComparatorFactory(true);
            case "rating": return new RatingComparatorFactory(false);
            case "-rating": return new RatingComparatorFactory(true);
            case "reviews": return new ReviewsComparatorFactory(false);
            case "-reviews": return new ReviewsComparatorFactory(true);
            case "salary": return new SalaryComparatorFactory(false);
            case "-salary": return new SalaryComparatorFactory(true);
            default: return new NoComparatorFactory();
        }
    }
}

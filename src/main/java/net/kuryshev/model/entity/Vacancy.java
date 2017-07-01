package net.kuryshev.model.entity;

/**
 * Created by 1 on 25.06.2017.
 */
public class Vacancy {
    private int id;
    private String title, description, salary, city, siteName, url;
    private double rating;
    private Company company;

    public Vacancy(String title, String description, String salary, String city, String siteName, String url, double rating, Company company) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.city = city;
        this.siteName = siteName;
        this.url = url;
        this.rating = rating;
        this.company = company;
    }

    public Vacancy() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (Double.compare(vacancy.rating, rating) != 0) return false;
        if (title != null ? !title.equals(vacancy.title) : vacancy.title != null) return false;
        if (description != null ? !description.equals(vacancy.description) : vacancy.description != null) return false;
        if (salary != null ? !salary.equals(vacancy.salary) : vacancy.salary != null) return false;
        if (city != null ? !city.equals(vacancy.city) : vacancy.city != null) return false;
        if (siteName != null ? !siteName.equals(vacancy.siteName) : vacancy.siteName != null) return false;
        if (url != null ? !url.equals(vacancy.url) : vacancy.url != null) return false;
        return company != null ? company.equals(vacancy.company) : vacancy.company == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }
}

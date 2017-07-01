package net.kuryshev.model.entity;

/**
 * Created by 1 on 01.07.2017.
 */
public class Company {
    private int id;
    private String name, url, rewiewsUrl;
    private double rating;

    public Company(String name, String url, String rewiewsUrl, double rating) {
        this.name = name;
        this.url = url;
        this.rewiewsUrl = rewiewsUrl;
        this.rating = rating;
    }

    public Company() {
        this.name = "empty";
        this.url = "";
        this.rewiewsUrl = "";
        this.rating = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRewiewsUrl() {
        return rewiewsUrl;
    }

    public void setRewiewsUrl(String rewiewsUrl) {
        this.rewiewsUrl = rewiewsUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (Double.compare(company.rating, rating) != 0) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (url != null ? !url.equals(company.url) : company.url != null) return false;
        return rewiewsUrl != null ? rewiewsUrl.equals(company.rewiewsUrl) : company.rewiewsUrl == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (rewiewsUrl != null ? rewiewsUrl.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

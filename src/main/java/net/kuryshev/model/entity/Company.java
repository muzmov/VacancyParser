package net.kuryshev.model.entity;

public class Company {
    private int id;
    private String name, url, reviewsUrl;
    private double rating;

    public Company(String name, String url, String reviewsUrl, double rating) {
        this.name = name;
        this.url = url;
        this.reviewsUrl = reviewsUrl;
        this.rating = rating;
    }

    public Company() {
        this.name = "empty";
        this.url = "";
        this.reviewsUrl = "";
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

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
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
        return reviewsUrl != null ? reviewsUrl.equals(company.reviewsUrl) : company.reviewsUrl == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (reviewsUrl != null ? reviewsUrl.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Company{" + "name='" + name + '\'' + ", url='" + url + '\'' + ", reviewsUrl='" + reviewsUrl + '\'' + ", rating=" + rating + '}';
    }
}

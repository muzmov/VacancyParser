package net.kuryshev.model.dao;

import net.kuryshev.model.entity.Company;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CompanyDao {

    List<Company> selectAll();

    Company getCompanyByName(String companyName);

    Map<String, Company> getCompaniesByNames(Set<String> companyNames);

    void add(Company company);

    void addAll(List<Company> company);

    void deleteAll();

    void update(Company company);
}

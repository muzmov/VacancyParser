package net.kuryshev.model.dao;

import net.kuryshev.model.entity.Company;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CompanyDao extends Dao {

    public abstract List<Company> selectAll();

    public abstract Company getCompanyByName(String companyName);

    public abstract Map<String, Company> getCompaniesByNames(Set<String> companyNames);

    public abstract void add(Company company);

    public abstract void addAll(List<Company> company);

    public abstract void deleteAll();

    public abstract void update(Company company);
}

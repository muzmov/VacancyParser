package net.kuryshev.model.dao;

import net.kuryshev.model.entity.Company;

import java.util.List;

public interface CompanyDao {

    List<Company> selectAll();

    void add(Company company);

    void addAll(List<Company> company);

    void deleteAll();

    void update(Company company);
}

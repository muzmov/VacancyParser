package net.kuryshev.model.strategy;

import net.kuryshev.model.dao.CompanyDao;
import net.kuryshev.model.dao.CompanyDaoJdbc;
import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;

import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public abstract class AbstractCompanyStrategy implements CompanyStrategy {
    protected static Logger logger = Logger.getLogger(getClassName());
    private final static int MAX_FAILS = 3;
    private CompanyDao dao =  new CompanyDaoJdbc();
    protected int failedAttempts;


    @Override
    public void fillCompaniesInfo() {
        dao.setProperties("../webapps/VacancyParser/WEB-INF/classes/dao.properties");
        List<Company> companies = dao.selectAll();
        for(Company company : companies) {
            if (company.getRewiewsUrl().equals("")) fillExternalCompanyInfo(company);
            if (failedAttempts > MAX_FAILS) {
                logger.error("Too many failed attempts. Parsing is stopped.");
                break;
            }
        }
        dao.deleteAll();
        dao.addAll(companies);
    }

    abstract void fillExternalCompanyInfo(Company company);
}

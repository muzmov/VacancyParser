package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class OraboteStrategyTest {

    @Ignore
    @Test
    public void getDocumentWithPost() {
        Document document = null;
        document = new OraboteStrategy().getDocument("https://orabote.top/feedback/search", "ростелеком");
        Assert.assertNotNull(document);
    }

    @Test
    @Ignore
    public void setExternalCompanyInfo() {
        Company company = new Company();
        company.setName("ростелеком");
        new OraboteStrategy().fillExternalCompanyInfo(company);
        System.out.println(company);
        Assert.assertNotEquals(company.getRewiewsUrl(), "");
        Assert.assertNotEquals(company.getRating(), 0);
    }

    @Test
    @Ignore
    public void setExternalCompanyInfoForIncorrectCompany() {
        Company company = new Company();
        company.setName("ростеdsfsdgлеком");
        new OraboteStrategy().fillExternalCompanyInfo(company);
        Assert.assertEquals("", company.getRewiewsUrl());
        Assert.assertEquals(0.0, company.getRating(), 0.0001);
    }
}
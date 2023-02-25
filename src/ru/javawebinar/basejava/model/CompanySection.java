package ru.javawebinar.basejava.model;

import java.util.List;

public class CompanySection extends AbstractSection {
    private List<Company> companies;

    @Override
    public void printContent() {
        for (Company company : companies) {
            company.printCompany();
        }
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        return companies.equals(that.companies);
    }

    @Override
    public int hashCode() {
        return companies.hashCode();
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "companies=" + companies +
                '}';
    }
}

package ru.javawebinar.basejava.model;

import java.util.List;

public class Company {

    private String name;
    private String webSite;
    private List<Period> periods;

    public void printCompany() {
        System.out.print(name);
        System.out.print(webSite != null ? " " + webSite + "\n" : "\n");
        for (Period period : periods) {
            period.printPeriod();
        }
        System.out.println();
    }

    public String getName() {
        return name;
    }

    public String getWebSite() {
        return webSite;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public Company(String name, List<Period> periods) {
        this(name, null, periods);
    }

    public Company(String name, String webSite, List<Period> periods) {
        this.name = name;
        this.webSite = webSite;
        this.periods = periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!name.equals(company.name)) return false;
        if (!webSite.equals(company.webSite)) return false;
        return periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + webSite.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", webSite='" + webSite + '\'' +
                ", periods=" + periods +
                '}';
    }
}

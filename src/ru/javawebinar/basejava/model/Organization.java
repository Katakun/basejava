package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

/**
 * gkislin
 * 19.07.2016
 */
public class Organization {
    private final String title;
    private final Link homePage;
    private final List<Period> periods;

    public Organization(String title, Link homePage, List<Period> periods) {
        Objects.requireNonNull(title, "title mast be not null");
        Objects.requireNonNull(periods, "periods mast be not null");
        this.title = title;
        this.homePage = homePage;
        this.periods = periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!title.equals(that.title)) return false;
        if (!Objects.equals(homePage, that.homePage)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (homePage != null ? homePage.hashCode() : 0);
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "title='" + title + '\'' +
                ", homePage=" + homePage +
                ", periods=" + periods +
                '}';
    }
}

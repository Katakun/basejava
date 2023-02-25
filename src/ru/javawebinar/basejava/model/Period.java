package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class Period {

    private String position;
    private String description;

    private LocalDate fromDate;
    private LocalDate endDate;

    public void printPeriod() {
        System.out.println(fromDate.getMonthValue() + "/" + fromDate.getYear() + "-" +
                endDate.getMonthValue() + "/" + endDate.getYear());
        System.out.println(position);
        System.out.print(description != null ? description + "\n": "");
    }

    public Period(String position, LocalDate fromDate, LocalDate endDate) {
        this(position,null, fromDate, endDate);
    }

    public Period(String position, String description, LocalDate fromDate, LocalDate endDate) {
        this.position = position;
        this.description = description;
        this.fromDate = fromDate;
        this.endDate = endDate;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!position.equals(period.position)) return false;
        if (!description.equals(period.description)) return false;
        if (!fromDate.equals(period.fromDate)) return false;
        return endDate.equals(period.endDate);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + fromDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", fromDate=" + fromDate +
                ", endDate=" + endDate +
                '}';
    }
}

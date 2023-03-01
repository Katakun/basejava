package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private final String position;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Period(String position, String description,
                  LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(position, "position must not be null");
        this.position = position;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

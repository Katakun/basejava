package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * gkislin
 * 14.07.2016
 */
public class ListSection extends Section {

    private static final long serialVersionUID = 1L;

    private List<String> items;

    public ListSection() {
    }

    public ListSection(String items) {
        this(Arrays.stream(items.trim().split("\n"))
                .map(s -> s.trim())
                .collect(Collectors.toList()));
    }

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> items) {
        Objects.requireNonNull(items, "items must not be null");
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public String getAllItemsAsString() {
        StringBuilder sb = new StringBuilder();
        for (String s : items) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return items.equals(that.items);

    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}


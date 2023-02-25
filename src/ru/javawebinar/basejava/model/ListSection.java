package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private List<String> descriptions;

    @Override
    public void printContent() {
        for (String description : descriptions) {
            System.out.println("\u2022 " + description);
        }
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public ListSection(List<String> description) {
        this.descriptions = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return descriptions.equals(that.descriptions);
    }

    @Override
    public int hashCode() {
        return descriptions.hashCode();
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "description=" + descriptions +
                '}';
    }
}

package ru.javawebinar.basejava.model;

public class TextSection extends AbstractSection {
    private String description;

    @Override
    public void printContent() {
        System.out.println(description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TextSection(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "description='" + description + '\'' +
                '}';
    }


}

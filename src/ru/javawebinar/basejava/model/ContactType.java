package ru.javawebinar.basejava.model;

public enum ContactType {
    TELEPHONE("Тел."),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private final String type;

    public String getType() {
        return type;
    }

    ContactType(String type) {
        this.type = type;
    }
}

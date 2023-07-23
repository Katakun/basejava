package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел."),
    MOBILE("Мобильный"),
    HOME_PHONE("Домашний тел."),
    SKYPE("Skype") {
        @Override
        public String link(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    MAIL("Почта") {
        @Override
        public String link(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN("LinkedIn") {
        @Override
        public String link(String value) {
            return "<a href=https://www.linkedin.com/" + value + ">" + value + "</a>";
        }
    },
    GITHUB("GitHub") {
        @Override
        public String link(String value) {
            return "<a href=https://www.github.com/" + value + ">" + value + "</a>";
        }
    },
    STACKOVERFLOW("Stackoverflow") {
        @Override
        public String link(String value) {
            return "<a href=https://stackoverflow.com/" + value + ">" + value + "</a>";
        }
    },
    HOME_PAGE("Домашняя страница") {
        @Override
        public String link(String value) {
            return "<a href=https://" + value + ">" + value + "</a>";
        }
    };

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String link(String value) {
        return value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : title + ": " + link(value);
    }
}

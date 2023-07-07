package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = new Resume(UUID_1, "Иванов Первый");
        R2 = new Resume(UUID_2, "Петров Второй");
        R3 = new Resume(UUID_3, "Сидоров Третий");
        R4 = new Resume(UUID_4, "Федоров Четвертый");

        R1.addContact(ContactType.MAIL, "ivanov@mail.ru");
        R1.addContact(ContactType.PHONE, "111-11-11");

        R2.addContact(ContactType.MAIL, "petrov@mail.ru");
        R2.addContact(ContactType.PHONE, "222-22-22");
        R2.addContact(ContactType.SKYPE, "skype222");

        R3.addContact(ContactType.MAIL, "sidorov@mail.ru");
        R3.addContact(ContactType.PHONE, "333-33-33");

        R4.addContact(ContactType.MAIL, "fedorov@mail.ru");
        R4.addContact(ContactType.PHONE, "444-44-44");
        R4.addContact(ContactType.SKYPE, "Skype444");

        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective1"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data"));
        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("Achivment11", "Achivment12", "Achivment13"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization11", "http://Organization11.ru",
                                new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization2", "http://Organization2.ru",
                                new Organization.Position(2015, Month.JANUARY, "position1", "content1"))));
        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Institute", null,
                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
                        new Organization("Organization12", "http://Organization12.ru")));

    }
}
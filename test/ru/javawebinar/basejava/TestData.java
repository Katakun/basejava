package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;

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
        R1 = new Resume(UUID_1, "Григорий Кислин");
        R2 = new Resume(UUID_2, "Петров Петр");
        R3 = new Resume(UUID_3, "Сидоров Сидор");
        R4 = new Resume(UUID_4, "Федоров Федор");

        R1.addContact(ContactType.PHONE, "111-11-11");
        R1.addContact(ContactType.SKYPE, "grigory.kislin");
        R1.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        R1.addContact(ContactType.LINKEDIN, "gkislin");
        R1.addContact(ContactType.GITHUB, "gkislin");
        R1.addContact(ContactType.STACKOVERFLOW, "548473");

        R2.addContact(ContactType.MAIL, "petrov@mail.ru");
        R2.addContact(ContactType.PHONE, "222-22-22");
        R2.addContact(ContactType.SKYPE, "skype222");

        R3.addContact(ContactType.MAIL, "sidorov@mail.ru");
        R3.addContact(ContactType.PHONE, "333-33-33");

        R4.addContact(ContactType.MAIL, "fedorov@mail.ru");
        R4.addContact(ContactType.PHONE, "444-44-44");
        R4.addContact(ContactType.SKYPE, "Skype444");

        R1.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("" +
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков",
                "разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов",
                "Реализация протоколов по приему платежей всех основных платежных системы России"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("" +
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy",
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts",
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring",
                "Python: Django.",
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX",
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix",
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI,",
                "Отличное знание и опыт применения концепций ООП, SOA",
                "Родной русский, английский \"upper intermediate\"",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB"));

        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Java Online Projects", "http://javaops.ru/",
                                new Organization.Position(2013, Month.JANUARY,
                                        "Автор проекта.",
                                        "Создание, организация и проведение Java онлайн проектов и стажировок.")),
                        new Organization("Wrike", "https://www.wrike.com/",
                                new Organization.Position(2014, Month.NOVEMBER, 2016, Month.JANUARY,
                                        "Старший разработчик (backend)",
                                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, " +
                                                "Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная " +
                                                "аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.")),
                        new Organization("RIT Center", null,
                                new Organization.Position(2012, Month.APRIL, 2014, Month.NOVEMBER,
                                        "Java архитектор",
                                        "Организация процесса разработки системы ERP для разных окружений: " +
                                                "релизная политика, версионирование, ведение CI (Jenkins), миграция базы " +
                                                "(кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), " +
                                                "AAA via SSO. "))
                ));

        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Coursera", "https://www.coursera.org/course/progfun",
                                new Organization.Position(2013, Month.MARCH, 2013, Month.MAY,
                                        "'Functional Programming Principles in Scala' by Martin Odersky",
                                        null)),
                        new Organization("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                                new Organization.Position(2011, Month.MARCH, 2011, Month.APRIL,
                                        "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'",
                                        "")),
                        new Organization("Siemens AG", "http://www.siemens.ru/",
                                new Organization.Position(2005, Month.JANUARY, 2005, Month.APRIL,
                                        "3 месяца обучения мобильным IN сетям (Берлин)",
                                        ""))

                ));

    }
}
package ru.javawebinar.basejava.model;

import java.util.EnumMap;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private final EnumMap<ContactType, String> contacts;
    private final EnumMap<SectionType, AbstractSection> sections;

    public void printResume() {
        System.out.println(fullName + "\n");
        for (ContactType key : contacts.keySet()) {
            System.out.println(key.getType() + ": " +contacts.get(key));
        }
        System.out.println();
        for (SectionType key: sections.keySet()) {
            System.out.println(key.getTitle());
            sections.get(key).printContent();
            System.out.println();
        }
    }

    public Resume() {
        this(UUID.randomUUID().toString(), "", null, null);
    }

    public Resume(String uuid) {
        this(uuid, "", null, null);
    }

    public Resume(String uuid, String fullName) {
        this(uuid, fullName, null, null);
    }

    public Resume(String fullName,
                  EnumMap<ContactType, String> contacts,
                  EnumMap<SectionType, AbstractSection> sections) {
        this(UUID.randomUUID().toString(), fullName,
                contacts, sections);
    }

    public Resume(String uuid, String fullName,
                  EnumMap<ContactType, String> contacts,
                  EnumMap<SectionType, AbstractSection> sections) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = contacts;
        this.sections = sections;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public EnumMap<ContactType, String> getContacts() {
        return contacts;
    }

    public EnumMap<SectionType, AbstractSection> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        if (!fullName.equals(resume.fullName)) return false;
        if (!contacts.equals(resume.contacts)) return false;
        return sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + contacts.hashCode();
        result = 31 * result + sections.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
    }
}
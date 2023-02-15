package ru.javawebinar.basejava.model;

import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private final String fullName;

    public Resume() {
        this(UUID.randomUUID().toString(), "Dummy Name");
    }

    public Resume(String uuid) {
        this(uuid, "Dummy Name");
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid)
                && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
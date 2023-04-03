package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DataStreamSerializer implements StreamSerializer {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FunctionalInterface
    public interface DosWriter<T> {
        void write(T t) throws IOException;
    }

    private void writeWithException(Collection c, DataOutputStream dos, DosWriter writer) throws IOException {
        dos.writeInt(c.size());
        Objects.requireNonNull(writer);
        for (Object o : c) {
            writer.write(o);
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {

        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            writeWithException(contacts.entrySet(), dos, (o) -> {
                Map.Entry entry = (Map.Entry) o;
                Enum key = (Enum) entry.getKey();
                dos.writeUTF(key.name());
                dos.writeUTF((String) entry.getValue());
            });

            // TODO implements sections
            Map<SectionType, Section> sections = r.getSections();

            writeWithException(sections.entrySet(), dos, (o) -> {
                Map.Entry entry = (Map.Entry) o;
                SectionType sectionType = (SectionType) entry.getKey();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = (ListSection) entry.getValue();
                        List<String> stringList = listSection.getItems();
                        writeWithException(stringList, dos, item -> dos.writeUTF((String) item));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        List<Organization> organizationList = organizationSection.getOrganizations();
                        writeWithException(organizationList, dos, o1 -> {
                            Organization org = (Organization) o1;
                            dos.writeUTF(org.getHomePage().getName());
                            if (org.getHomePage().getUrl() != null) {
                                dos.writeBoolean(true);
                                dos.writeUTF(org.getHomePage().getUrl());
                            } else {
                                dos.writeBoolean(false);
                            }
                            List<Organization.Position> positionList = org.getPositions();
                            writeWithException(positionList, dos, p -> {
                                Organization.Position position = (Organization.Position) p;
                                dos.writeUTF(position.getStartDate().format(formatter));
                                dos.writeUTF(position.getEndDate().format(formatter));
                                dos.writeUTF(position.getTitle());
                                if (position.getDescription() != null) {
                                    dos.writeBoolean(true);
                                    dos.writeUTF(position.getDescription());
                                } else {
                                    dos.writeBoolean(false);
                                }
                            });
                        });
                }
            });


//            dos.writeInt(sections.size());
//            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
//                String sectionName = entry.getKey().name();
//                dos.writeUTF(sectionName);
//                SectionType sectionType = SectionType.valueOf(sectionName);
//                switch (sectionType) {
//                    case OBJECTIVE:
//                    case PERSONAL:
//                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
//                        break;
//                    case ACHIEVEMENT:
//                    case QUALIFICATIONS:
//                        ListSection listSection = (ListSection) entry.getValue();
//                        List<String> listStrings = listSection.getItems();
//                        dos.writeInt(listStrings.size());
//                        for (String s : listStrings) {
//                            dos.writeUTF(s);
//                        }
//                        break;
//                    case EXPERIENCE:
//                    case EDUCATION:
//                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
//                        List<Organization> listOrganizations = organizationSection.getOrganizations();
//                        dos.writeInt(listOrganizations.size());
//                        for (Organization org : listOrganizations) {
//                            dos.writeUTF(org.getHomePage().getName());
//                            if (org.getHomePage().getUrl() != null) {
//                                dos.writeBoolean(true);
//                                dos.writeUTF(org.getHomePage().getUrl());
//                            } else {
//                                dos.writeBoolean(false);
//                            }
//                            List<Organization.Position> listPos = org.getPositions();
//                            dos.writeInt(listPos.size());
//                            for (Organization.Position p : listPos) {
//                                dos.writeUTF(p.getStartDate().format(formatter));
//                                dos.writeUTF(p.getEndDate().format(formatter));
//                                dos.writeUTF(p.getTitle());
//                                if (p.getDescription() != null) {
//                                    dos.writeBoolean(true);
//                                    dos.writeUTF(p.getDescription());
//                                } else {
//                                    dos.writeBoolean(false);
//                                }
//                            }
//                        }
//                        break;
//                }
//            }
        } catch (IOException e) {
            throw new StorageException("DataOutputStream error", r.getUuid(), e);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactSize = dis.readInt();
            for (int i = 0; i < contactSize; i++) {
                ContactType type = ContactType.valueOf(dis.readUTF());
                String contact = dis.readUTF();
                resume.addContact(type, contact);
            }
            // TODO implements sections
            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int listSize = dis.readInt();
                        List<String> items = new ArrayList<>();
                        for (int j = 0; j < listSize; j++) {
                            items.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(items));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizationList = new ArrayList<>();
                        int orgSize = dis.readInt();
                        for (int j = 0; j < orgSize; j++) {
                            String name = dis.readUTF();
                            String url = dis.readBoolean() ? dis.readUTF() : null;
                            Link link = new Link(name, url);
                            int posSize = dis.readInt();
                            List<Organization.Position> positions = new ArrayList<>();
                            for (int k = 0; k < posSize; k++) {
                                LocalDate start = LocalDate.parse(dis.readUTF(), formatter);
                                LocalDate end = LocalDate.parse(dis.readUTF(), formatter);
                                String title = dis.readUTF();
                                String description = dis.readBoolean() ? dis.readUTF() : null;
                                positions.add(new Organization.Position(start, end, title, description));
                            }
                            organizationList.add(new Organization(link, positions));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            }
            return resume;
        } catch (IOException e) {
            throw new StorageException("DataInputStream error", e);
        }
    }
}

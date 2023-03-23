package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            // TODO implements sections
            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                switch (entry.getKey().name()) {
                    // TextSection
                    case "OBJECTIVE":
                    case "PERSONAL":
                        dos.writeUTF(entry.getKey().name());
                        dos.writeUTF(entry.getValue().toString());
                        break;
                    // ListSection
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        dos.writeUTF(entry.getKey().name());
                        ListSection listSection = (ListSection) entry.getValue();
                        List<String> listStrings = listSection.getItems();
                        dos.writeInt(listStrings.size());
                        for (String s : listStrings) {
                            dos.writeUTF(s);
                        }
                        break;
                    // OrganizationsSection
                    case "EXPERIENCE":
                    case "EDUCATION":
                        dos.writeUTF(entry.getKey().name());
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        List<Organization> listOrganizations = organizationSection.getOrganizations();
                        dos.writeInt(listOrganizations.size());
                        for (Organization org : listOrganizations) {
                            dos.writeUTF(org.getHomePage().getName());
                            if (org.getHomePage().getUrl() != null) {
                                dos.writeBoolean(true);
                                dos.writeUTF(org.getHomePage().getUrl());
                            } else {
                                dos.writeBoolean(false);
                            }
                            List<Organization.Position> listPos = org.getPositions();
                            dos.writeInt(listPos.size());
                            for (Organization.Position p : listPos) {
                                dos.writeUTF(String.valueOf(p.getStartDate()));
                                dos.writeUTF(String.valueOf(p.getEndDate()));
                                dos.writeUTF(p.getTitle());
                                if (p.getDescription() != null) {
                                    dos.writeBoolean(true);
                                    dos.writeUTF(p.getDescription());
                                } else {
                                    dos.writeBoolean(false);
                                }
                            }
                        }
                        break;
                }
            }
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
                    // TextSection
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    // ListSection
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int listSize = dis.readInt();
                        List<String> items = new ArrayList<>();
                        for (int j = 0; j < listSize; j++) {
                            items.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(items));
                        break;
                    // OrganizationSection
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizationList = new ArrayList<>();
                        int orgSize = dis.readInt();
                        for (int j = 0; j < orgSize; j++) {
                            String name = dis.readUTF();
                            Link link;
                            String url = null;
                            if (dis.readBoolean()) {
                                url = dis.readUTF();
                            }
                            link = new Link(name, url);
                            int posSize = dis.readInt();
                            List<Organization.Position> positions = new ArrayList<>();
                            for (int k = 0; k < posSize; k++) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                                formatter = formatter.withLocale(Locale.US);
                                LocalDate start = LocalDate.parse(dis.readUTF(), formatter);
                                LocalDate end = LocalDate.parse(dis.readUTF(), formatter);
                                String title = dis.readUTF();
                                String description = null;
                                if (dis.readBoolean()) {
                                    description = dis.readUTF();
                                }
                                positions.add(new Organization.Position(start, end, title, description));
                            }
                            organizationList.add(new Organization(link, positions));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            }
            return resume;
        }
    }
}

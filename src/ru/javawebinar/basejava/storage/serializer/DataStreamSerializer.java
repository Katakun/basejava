package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FunctionalInterface
    public interface DosWriter<T> {
        void write(T t) throws IOException;
    }

    public interface DosReader {
        void read() throws IOException;
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {

        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(r.getContacts().entrySet(), dos, o -> {
                Map.Entry entry = (Map.Entry) o;
                dos.writeUTF(((Enum) entry.getKey()).name());
                dos.writeUTF((String) entry.getValue());
            });

            // TODO implements sections
            writeWithException(r.getSections().entrySet(), dos, o -> {
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
                            writeWithException(org.getPositions(), dos, p -> {
                                Organization.Position position = (Organization.Position) p;
                                dos.writeUTF(position.getStartDate().format(FORMATTER));
                                dos.writeUTF(position.getEndDate().format(FORMATTER));
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
            readWithException(dis, resume,() -> {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String contact = dis.readUTF();
                resume.addContact(contactType, contact);
            });

            // TODO implements sections
            readWithException(dis,resume, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = new ArrayList<>();
                        readWithException(dis,resume, () -> {
                            items.add(dis.readUTF());
                        });
                        resume.addSection(sectionType, new ListSection(items));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizationList = new ArrayList<>();
                        readWithException(dis, resume, () -> {
                            String name = dis.readUTF();
                            String url = dis.readBoolean() ? dis.readUTF() : null;
                            Link link = new Link(name, url);
                            List<Organization.Position> positions = new ArrayList<>();
                            readWithException(dis, resume, () -> {
                                LocalDate start = LocalDate.parse(dis.readUTF(), FORMATTER);
                                LocalDate end = LocalDate.parse(dis.readUTF(), FORMATTER);
                                String title = dis.readUTF();
                                String description = dis.readBoolean() ? dis.readUTF() : null;
                                positions.add(new Organization.Position(start, end, title, description));
                            });
                            organizationList.add(new Organization(link, positions));
                        });
                        resume.addSection(sectionType, new OrganizationSection(organizationList));
                }
            });
        return resume;
        }

    }

    private <T> void writeWithException(Collection<T> c, DataOutputStream dos, DosWriter writer) throws IOException {
        dos.writeInt(c.size());
        Objects.requireNonNull(writer);
        for (T t : c) {
            writer.write(t);
        }
    }

    private void readWithException(DataInputStream dis, Resume resume, DosReader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }
}

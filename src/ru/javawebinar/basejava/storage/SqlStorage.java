package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO implement Section (except OrganizationSection)
// TODO Join and split ListSection by `\n`
public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        "    LEFT JOIN contact c " +
                        "           ON r.uuid = c.resume_uuid " +
                        "    LEFT JOIN section s " +
                        "           ON r.uuid = s.resume_uuid" +
                        "        WHERE r.uuid = ? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, r);
                        addSection(rs, r);
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "UPDATE resume " +
                    "   SET full_name = ? " +
                    " WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            deleteSections(conn, r);
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "INSERT INTO resume (uuid, full_name) " +
                    "     VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("" +
                "DELETE FROM resume " +
                "      WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumeMap = new LinkedHashMap<>();
            try (PreparedStatement psResume = conn.prepareStatement("" +
                    "SELECT * FROM resume r " +
                    "     ORDER BY full_name, uuid")) {
                final ResultSet rs = psResume.executeQuery();
                while (rs.next()) {
                    final String uuid = rs.getString("uuid");
                    resumeMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement psContact = conn.prepareStatement("" +
                    "SELECT * FROM contact c ")) {
                final ResultSet rs = psContact.executeQuery();
                while (rs.next()) {
                    final Resume resume = resumeMap.get(rs.getString("resume_uuid"));
                    addContact(rs, resume);
                }
            }
            try (PreparedStatement psSection = conn.prepareStatement("" +
                    "SELECT * FROM section s ")) {
                final ResultSet rs = psSection.executeQuery();
                while (rs.next()) {
                    final Resume resume = resumeMap.get(rs.getString("resume_uuid"));
                    addSection(rs, resume);
                }
            }
            return new ArrayList<>(resumeMap.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("" +
                "SELECT count(*) " +
                "  FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("" +
                "INSERT INTO contact (resume_uuid, contact_type, contact_value) " +
                "     VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("" +
                "INSERT INTO section (resume_uuid, section_type, section_value) " +
                "     VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, getStringFromSection(e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String getStringFromSection(Section section) {
        StringBuilder result = new StringBuilder();
        String className = section.getClass().getSimpleName();
        switch (className) {
            case "ListSection":
                List<String> list = ((ListSection) section).getItems();
                for (String s : list) {
                    result.append(s).append("\n");
                }
                break;
            case "TextSection":
                result.append(((TextSection) section).getContent());
                break;
            default:
                throw new IllegalStateException("Wrong section type");
        }
        return result.toString();
    }

    private Section getSectionFromString(String type, String value) {
        if (type.equals("PERSONAL") || type.equals("OBJECTIVE")) {
            return new TextSection(value);
        } else {
            String[] values = value.split("\n");
            return new ListSection(values);
        }
    }

    private void deleteContacts(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM contact WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void deleteSections(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM section WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String section_type = rs.getString("section_type");
        String section_value = rs.getString("section_value");
        if (null != section_type && null != section_value) {
            Section section = getSectionFromString(section_type, section_value);
            r.addSection(SectionType.valueOf(section_type), section);
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("contact_value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("contact_type")), value);
        }
    }
}

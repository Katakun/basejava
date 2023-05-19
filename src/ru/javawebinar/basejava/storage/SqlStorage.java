package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ABlockOfCode;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = new ConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        };
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM resume");
                ps.execute();
            }
        });

    }

    @Override
    public Resume get(String uuid) {
        final Resume[] resultResume = new Resume[1];
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?");
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resultResume[0] = new Resume(uuid, rs.getString("full_name"));
            }

        });
        return resultResume[0];
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?");
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                ps.execute();
            }
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement psCheck = conn.prepareStatement("SELECT uuid FROM resume WHERE uuid=?");
                psCheck.setString(1, r.getUuid());
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next()) {
                    throw new ExistStorageException(r.getUuid());
                }
                PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)");
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
        });
    }

    public void delete(String uuid) {
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement psGet = conn.prepareStatement("SELECT uuid FROM resume WHERE uuid=?");
                PreparedStatement ps = conn.prepareStatement("DELETE FROM resume WHERE uuid=?");
                psGet.setString(1, uuid);
                ResultSet resultSet = psGet.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                ps.setString(1, uuid);
                ps.execute();
            }
        });
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY uuid");
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                }

            }
        });
        return resumes;
    }

    public int size() {
        final int[] size = new int[1];
        sqlHelper.execute(new ABlockOfCode() {
            @Override
            public void someCode(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM resume");
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                size[0] = resultSet.getInt(1);
            }
        });
        return size[0];
    }
}
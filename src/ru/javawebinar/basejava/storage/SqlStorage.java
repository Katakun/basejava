package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", (preparedStatement) -> {
            preparedStatement.execute();
        });
    }

    @Override
    public Resume get(String uuid) {
        Resume[] resultResume = new Resume[1];
        sqlHelper.execute("SELECT * FROM resume r WHERE r.uuid =?", preparedStatement -> {
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            resultResume[0] = new Resume(uuid, rs.getString("full_name"));
        });
        return resultResume[0];
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute("UPDATE resume SET full_name=? WHERE uuid=?", preparedStatement -> {
            preparedStatement.setString(1, r.getFullName());
            preparedStatement.setString(2, r.getUuid());
            preparedStatement.execute();
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", preparedStatement -> {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.setString(2, r.getFullName());
            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new ExistStorageException(r.getUuid());
            }
        });
    }

    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (!preparedStatement.execute()) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.execute("SELECT * FROM resume ORDER BY uuid", preparedStatement -> {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }

        });
        return resumes;
    }

    public int size() {
        final int[] size = new int[1];
        sqlHelper.execute("SELECT count(*) FROM resume", preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            size[0] = resultSet.getInt(1);
        });
        return size[0];
    }
}
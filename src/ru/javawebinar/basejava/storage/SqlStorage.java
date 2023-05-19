package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "DELETE FROM resume");
        sqlHelper.execute((preparedStatement) -> {
            preparedStatement.execute();
        });
    }

    @Override
    public Resume get(String uuid) {
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "SELECT * FROM resume r WHERE r.uuid =?");
        Resume[] resultResume = new Resume[1];
        sqlHelper.execute(preparedStatement -> {
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
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "UPDATE resume SET full_name=? WHERE uuid=?");
        sqlHelper.execute(preparedStatement -> {
            preparedStatement.setString(1, r.getFullName());
            preparedStatement.setString(2, r.getUuid());
            preparedStatement.execute();
        });
    }

    @Override
    public void save(Resume r) {
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "INSERT INTO resume (uuid, full_name) VALUES (?,?)");
        sqlHelper.execute(preparedStatement -> {
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
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "DELETE FROM resume WHERE uuid=?");
        sqlHelper.execute(preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (!preparedStatement.execute()) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    public List<Resume> getAllSorted() {
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "SELECT * FROM resume ORDER BY uuid");
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.execute(preparedStatement -> {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }

        });
        return resumes;
    }

    public int size() {
        SqlHelper sqlHelper = new SqlHelper(connectionFactory, "SELECT count(*) FROM resume");
        final int[] size = new int[1];
        sqlHelper.execute(preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            size[0] = resultSet.getInt(1);
        });
        return size[0];
    }
}
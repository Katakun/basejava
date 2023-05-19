package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;
    private final String sql;

    public SqlHelper(ConnectionFactory connectionFactory, String sql) {
        this.connectionFactory = connectionFactory;
        this.sql = sql;
    }

    public void execute(ABlockOfCode aBlockOfCode) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            aBlockOfCode.someCode(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}

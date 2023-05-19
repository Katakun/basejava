package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

// getConnection(), prepareStatement, catch SQLException
// паттерн стратегия, лямбды,
public class SqlHelper {
    private ConnectionFactory connectionFactory;
    private ResultSet resultSet;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void execute(ABlockOfCode aBlockOfCode) {
        try (Connection conn = connectionFactory.getConnection()) {
            aBlockOfCode.someCode(conn);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}

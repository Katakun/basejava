package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ABlockOfCode {
    void someCode(Connection conn) throws SQLException;
}

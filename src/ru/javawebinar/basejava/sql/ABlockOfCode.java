package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ABlockOfCode {
    void someCode(PreparedStatement ps) throws SQLException;
}

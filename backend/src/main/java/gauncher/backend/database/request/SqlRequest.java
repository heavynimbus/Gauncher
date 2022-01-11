package gauncher.backend.database.request;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlRequest {
    protected final Statement statement;

    public SqlRequest(Connection connection) throws SQLException {
        this.statement = connection.createStatement();
    }
}
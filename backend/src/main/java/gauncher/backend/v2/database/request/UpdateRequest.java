package gauncher.backend.v2.database.request;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateRequest extends SqlRequest{
    public UpdateRequest(Connection connection) throws SQLException {
        super(connection);
    }
}

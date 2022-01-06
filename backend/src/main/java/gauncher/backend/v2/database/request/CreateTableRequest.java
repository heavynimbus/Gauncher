package gauncher.backend.v2.database.request;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateTableRequest extends SqlRequest {
    private String tableName;

    public CreateTableRequest(Connection connection) throws SQLException {
        super(connection);
    }

    public CreateTableRequest name(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(this.tableName).append(" ( ");
        return builder.append(" );").toString();
    }
}

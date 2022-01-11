package gauncher.backend.database.request;

import gauncher.backend.database.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertRequest<T extends Entity> extends SqlRequest {
    private String tableName;
    private List<T> values;

    public InsertRequest(Connection connection) throws SQLException {
        super(connection);
        this.values = new ArrayList<>();
    }

    public InsertRequest into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertRequest value(T entity) {
        this.values.add(entity);
        return this;
    }

    public void execute() throws SQLException {
        this.statement.executeUpdate(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES ");
        values.forEach(value -> builder.append(value.getInsertValueString()).append(","));
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}

package gauncher.backend.v2.database.request;

import gauncher.backend.v2.database.entity.Entity;

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
}

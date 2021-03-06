package gauncher.backend.database.request;

import gauncher.backend.logging.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SelectRequest extends SqlRequest {
    private final static Logger log = new Logger("SelectRequest");
    private String table;
    private String[] columns;
    private String whereClause;

    public SelectRequest(Connection connection) throws SQLException {
        super(connection);
    }

    public SelectRequest select(String... columns) {
        this.columns = columns;
        return this;
    }

    public SelectRequest from(String table) {
        this.table = table;
        return this;
    }

    public SelectRequest where(String whereClause) {
        this.whereClause = whereClause;
        return this;
    }

    public ResultSet execute() throws SQLException {
        log.debug("Execute query %s", this);
        return this.statement.executeQuery(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder request = new StringBuilder("SELECT");
        if (columns == null || columns.length == 0) request.append(" * ");
        else {
            Arrays.stream(columns).map((column) -> " " + column + ",").forEachOrdered(request::append);
            request.setLength(request.length() - 1);
        }
        request.append(" FROM ").append(table);

        if (whereClause != null) request.append(" WHERE ").append(whereClause);
        return request.append(";").toString();
    }

}


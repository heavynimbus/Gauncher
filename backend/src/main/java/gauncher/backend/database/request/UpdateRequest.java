package gauncher.backend.database.request;

import gauncher.backend.database.entity.Entity;
import gauncher.backend.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateRequest<T extends Entity> extends SqlRequest {
    private final static Logger log = new Logger("UpdateRequest");
    private final T entity;

    public UpdateRequest(Connection connection, T entity) throws SQLException {
        super(connection);
        this.entity = entity;
    }

    public int execute() throws SQLException {
        this.entity.prePersist();
        log.debug("Execute update: " + this);
        return this.statement.executeUpdate(this.toString());
        /*new SelectRequest(statement.getConnection())
                .from(this.entity.getTableName())
                .where(String.format("id = %s", this.entity.getId()))
                .execute();
        return null;*/
    }

    @Override
    public String toString() {
        return String.format("UPDATE %s SET %s WHERE id = %s", entity.getTableName(), entity.getUpdateValueString(), entity.getId());
    }
}

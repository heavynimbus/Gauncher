package gauncher.backend.database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public abstract class Entity {
    protected Integer id;
    protected Instant createdAt;
    protected Instant updatedAt;

    public Entity() {
        this.id = null;
        this.updatedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    public Entity(Integer id, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Entity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
    }

    public abstract String getInsertValueString();

    public abstract String getUpdateValueString();
}


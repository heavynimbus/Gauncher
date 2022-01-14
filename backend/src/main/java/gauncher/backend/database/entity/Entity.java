package gauncher.backend.database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

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
        this.createdAt = resultSet.getTimestamp("created_at").toInstant();
        this.updatedAt = resultSet.getTimestamp("updated_at").toInstant();
    }

    public abstract String getInsertColumnNames();

    public abstract String getInsertValueString();

    public abstract String getUpdateValueString();

    public abstract String getTableName();

    public Integer getId() {
        return id;
    }

    public void prePersist() {
        var now = Instant.now().atZone(ZoneId.of("Europe/Paris"));
        if (this.createdAt == null) this.createdAt = Instant.now(Clock.systemDefaultZone());
        this.updatedAt = Instant.now(Clock.systemUTC());
    }


}


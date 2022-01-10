package gauncher.backend.v2.database.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Entity {
    protected Integer id;
    protected Instant createdAt;
    protected Instant updatedAt;

    public Entity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        System.out.println("resultSet.getString(\"created_at\") = " + resultSet.getString("created_at"));
    }

    public abstract String getInsertValueString();

    public abstract String getUpdateValueString();
}

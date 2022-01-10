package gauncher.backend.v2.database.entity;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Getter
@Setter
@ToString
public class Client extends Entity {
    private String username;
    private String password;

    public Client(Integer id, Instant createdAt, Instant updatedAt, String username, String password) {
        super(id, createdAt, updatedAt);
        this.username = username;
        this.password = password;
    }

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Client(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.username = resultSet.getString("username");
        this.password = resultSet.getString("password");
    }

    @Override
    public String getInsertValueString() {
        return String.format("('%s','%s')", username, password);
    }

    @Override
    public String getUpdateValueString() {
        return null;
    }
}



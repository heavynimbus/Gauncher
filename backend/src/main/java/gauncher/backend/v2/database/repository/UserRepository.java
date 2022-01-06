package gauncher.backend.v2.database.repository;

import gauncher.backend.v2.database.DatabaseConnection;
import gauncher.backend.v2.database.entity.Client;
import gauncher.backend.v2.database.request.SelectRequest;
import gauncher.backend.v2.util.StringUtil;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends DatabaseConnection {

    public UserRepository(){
        super();
    }

    public Optional<Client> getByUsername(String username) throws SQLException {
        var rs = new SelectRequest(connection)
                .select()
                .from("client")
                .where(String.format("username = '%s'", StringUtil.shortify(username)))
                .execute();
        if (!rs.next()) return Optional.empty();
        return Optional.of(new Client(rs));
    }

    public Optional<Client> persist(Client client) {

        return Optional.empty();
    }

}

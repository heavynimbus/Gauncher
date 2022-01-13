package gauncher.backend.database.repository;

import gauncher.backend.database.DatabaseConnection;
import gauncher.backend.database.entity.Client;
import gauncher.backend.database.request.SelectRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientRepository extends DatabaseConnection {

    public Optional<Client> findByUsername(String username) {
        try {
            SelectRequest request = new SelectRequest(connection).select().from("client").where("username = '" + username + "'");
            ResultSet result = request.execute();
            if (result.next())
                return Optional.of(new Client(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

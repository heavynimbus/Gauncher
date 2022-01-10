package gauncher.backend.v2.database.repository;

import gauncher.backend.v1.logging.Logger;
import gauncher.backend.v2.database.DatabaseConnection;
import gauncher.backend.v2.database.entity.Client;
import gauncher.backend.v2.database.request.InsertRequest;
import gauncher.backend.v2.database.request.SelectRequest;
import gauncher.backend.v2.util.StringUtil;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends DatabaseConnection {
    private final static Logger log = new Logger("UserRepository");

    public UserRepository() {
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

    public Optional<Client> persist(Client client) throws SQLException {
        System.out.println("UserRepository.persist");
        if (client.getId() == null) {
            log.info(String.format("Create new client %s", client));
            new InsertRequest<Client>(connection).into("client(username, password)").value(client).execute();
        }
        return getByUsername(client.getUsername());
    }

}

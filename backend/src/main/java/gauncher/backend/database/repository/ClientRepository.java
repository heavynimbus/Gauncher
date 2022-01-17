package gauncher.backend.database.repository;

import gauncher.backend.database.DatabaseConnection;
import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.request.InsertRequest;
import gauncher.backend.database.request.SelectRequest;
import gauncher.backend.exception.AlreadyExistsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientRepository extends DatabaseConnection {

    public Optional<ClientEntity> findByUsername(String username) {
        try {
            SelectRequest request = new SelectRequest(connection).select()
                    .from("client")
                    .where("username = '" + username + "'");
            ResultSet result = request.execute();
            if (result.next())
                return Optional.of(new ClientEntity(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public ClientEntity create(ClientEntity clientEntity) throws AlreadyExistsException {
        try {
            new InsertRequest<ClientEntity>(connection).value(clientEntity).execute();
            return findByUsername(clientEntity.getUsername()).get();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AlreadyExistsException(String.format("Username %s already exists", clientEntity.getUsername()));
        }
    }
}

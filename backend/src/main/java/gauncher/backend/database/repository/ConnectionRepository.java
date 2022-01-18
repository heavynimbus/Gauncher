package gauncher.backend.database.repository;

import gauncher.backend.database.DatabaseConnection;
import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.entity.ConnectionEntity;
import gauncher.backend.database.request.InsertRequest;
import gauncher.backend.database.request.SelectRequest;
import gauncher.backend.database.request.UpdateRequest;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;

public class ConnectionRepository extends DatabaseConnection {
    private final static Logger log = new Logger("ConnectionRepository");

    public void create(ConnectionEntity connectionEntity) throws SQLException {
        var request = new InsertRequest<ConnectionEntity>(connection).value(connectionEntity);
        log.debug("Persisting new connection: %s", request);
        request.execute();
    }

    public void create(ClientEntity client) throws SQLException {
        this.create(new ConnectionEntity(client));
    }

    public void disconnect(ClientEntity client) throws SQLException {
        var whereClause = String.format("client_id = %s and socket_ip = '%s' and socket_port = %s and status = 'ACTIVE'",
                client.getId(),
                client.getSocket().getInetAddress(),
                client.getSocket().getPort());
        var currentConnection = new SelectRequest(connection).from("connection")
                .where(whereClause);
        System.out.println("currentConnection = " + currentConnection);
        var rs = currentConnection.execute();
        if (rs.next()) {
            ConnectionEntity connectionEntity = new ConnectionEntity(rs);
            connectionEntity.setStatus(ConnectionEntity.ConnectionStatus.INACTIVE);
            new UpdateRequest<>(this.connection, connectionEntity).execute();
        }
    }

    public Long getActiveCount() {
        try {
            var rs = new SelectRequest(connection).select("count(*) as count").from("connection").where("status = 'ACTIVE'").execute();
            if (rs.next())
                return rs.getLong("count");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }
}

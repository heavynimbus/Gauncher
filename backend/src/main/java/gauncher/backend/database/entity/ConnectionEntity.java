package gauncher.backend.database.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class ConnectionEntity extends Entity {

    private ConnectionStatus status;
    private final Integer clientId;
    private final String socketIp;
    private final Integer socketPort;

    public ConnectionEntity(ClientEntity clientEntity) {
        super();
        this.status = ConnectionStatus.ACTIVE;
        this.clientId = clientEntity.getId();
        this.socketIp = clientEntity.getSocket().getInetAddress().toString();
        this.socketPort = Integer.parseInt(clientEntity.getSocket().getRemoteSocketAddress().toString().split(":")[1]);
    }

    public ConnectionEntity(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.status = ConnectionStatus.valueOf(resultSet.getString("status"));
        this.clientId = resultSet.getInt("client_id");
        this.socketIp = resultSet.getString("socket_ip");
        this.socketPort = resultSet.getInt("socket_port");
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    @Override
    public String getInsertColumnNames() {
        return "(client_id, socket_ip, socket_port, status)";
    }

    @Override
    public String getInsertValueString() {
        return String.format("(%s, '%s', %s, '%s')", clientId, socketIp, socketPort, status);
    }

    @Override
    public String getUpdateValueString() {
        return String.format("updated_at = '%s', status = '%s'", updatedAt.atZone(ZoneId.of("Europe/Paris")), status);
    }

    @Override
    public String getTableName() {
        return "connection";
    }

    public enum ConnectionStatus {
        ACTIVE, INACTIVE
    }

    @Override
    public String toString() {
        return "ConnectionEntity{" +
                "id=" + id +
                ", status=" + status +
                ", clientId=" + clientId +
                ", socketIp='" + socketIp + '\'' +
                ", socketPort=" + socketPort +
                '}';
    }
}

package gauncher.backend.database;

import gauncher.backend.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {
    private final Logger log = new Logger("DatabaseConnection");
    protected Connection connection;

    public DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gauncher-db", "user", "supersecuredpass");
        } catch (SQLException e) {
            log.error("Make sure that your database is up, connection failed");
            this.connection = null;
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws SQLException {
        log.info("Close database connection");
        connection.close();
    }
}

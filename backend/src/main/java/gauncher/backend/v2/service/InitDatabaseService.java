package gauncher.backend.v2.service;

import gauncher.backend.v2.database.DatabaseConnection;
import gauncher.backend.v2.logging.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InitDatabaseService extends DatabaseConnection {
    private final static Logger log = new Logger("InitDatabaseService");

    public void init() throws Exception {
        log.info("Starting database initialization");
        var resource = this.getClass().getResource("/init_db.sql");
        Statement statement = this.connection.createStatement();
        Arrays.stream(Files.lines(Path.of(resource.toURI()))
                        .collect(Collectors.joining())
                        .split(";"))
                .forEach(request -> {
                    try {
                        if (request.startsWith("DROP") || request.startsWith("CREATE") || request.startsWith("INSERT")) {
                            log.debug("SQL: " + request);
                            statement.executeUpdate(request);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}

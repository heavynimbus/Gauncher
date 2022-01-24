package gauncher.backend.service;


import gauncher.backend.database.DatabaseConnection;
import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ClientRepository;
import gauncher.backend.logging.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InitDatabaseService extends DatabaseConnection {
    private final static Logger log = new Logger("InitDatabaseService");
    private final static ClientRepository clientRepository = new ClientRepository();
    private final static String INIT_DB_SQL = "/init-db.sql";
    public void init() throws Exception {
        log.info("Starting database initialization");
        var resource = this.getClass().getResource(INIT_DB_SQL);
        Statement statement = this.connection.createStatement();
        assert resource != null;
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
        clientRepository.create(new ClientEntity("user", "pass", true));
        clientRepository.create(new ClientEntity("toto", "toto", true));
        clientRepository.create(new ClientEntity("root", "root", true));
    }
}
package gauncher.backend;


import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.handler.LoginHandler;
import gauncher.backend.logging.Logger;
import gauncher.backend.service.InitDatabaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int SERVER_PORT = 8080;
    private static final Logger log = new Logger("ServerV2");

    private static void initDb() {
        try (InitDatabaseService initService = new InitDatabaseService()) {
            initService.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        initDb();
        var server = new ServerSocket(SERVER_PORT);
        log.info("Gauncher server is now waiting at port %s", server.getLocalPort());
        while (true) {
            Socket socket = server.accept();
            log.info("New connection from %s", socket.getRemoteSocketAddress());
            ClientEntity clientEntity = new ClientEntity(socket);
            new LoginHandler(clientEntity).start();
        }
    }
}

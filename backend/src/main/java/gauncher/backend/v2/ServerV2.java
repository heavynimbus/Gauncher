package gauncher.backend.v2;

import static java.lang.String.format;

import gauncher.backend.v2.handler.LoginHandler;
import gauncher.backend.v2.logging.Log;
import gauncher.backend.v2.logging.Logger;
import gauncher.backend.v2.player.Player;
import gauncher.backend.v2.service.InitDatabaseService;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

@Log
public class ServerV2 {
    private static final int SERVER_PORT = 8080;
    private static final Logger log = new Logger("ServerV2");

    private static Optional<ServerSocket> initServer() {
        try {
            return Optional.of(ServerSocketFactory.getDefault().createServerSocket(SERVER_PORT));
        } catch (IOException e) {
            log.error(format("Unable to start server at port %s", SERVER_PORT));
        }
        return Optional.empty();
    }

    public static void main(String[] args) throws Exception {
        new InitDatabaseService().init();
        var optionalServerSocket = initServer();
        if (optionalServerSocket.isEmpty()) return;
        ServerSocket server = optionalServerSocket.get();
        log.info(format("Gauncher server is now listening at port %d", 8080));
        while (true) {
            Socket client;
            Player player;
            try {
                client = server.accept();
                player = new Player(client);
            } catch (IOException e) {
                log.error("An IOException has occured on waiting for connection");
                e.printStackTrace();
                return;
            }
            log.info(format("New connection from %s", player));
            new LoginHandler(player, 0).start();
        }
    }
}

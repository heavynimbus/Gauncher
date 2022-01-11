package gauncher.backend;


import gauncher.backend.database.DatabaseConnection;
import gauncher.backend.database.entity.Client;
import gauncher.backend.handler.LoginHandler;
import gauncher.backend.logging.Logger;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class Server {
    private static final int SERVER_PORT = 8080;
    private static final Logger log = new Logger("ServerV2");

    /*
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
          log.error("An IOException has occured on waiting for connection or creating player");
          e.printStackTrace();
          return;
        }
        log.info(format("New connection from %s", player));
        new LoginHandler(player).start();
      }
    }*/


    public static void main(String[] args) throws IOException{
        var server = new ServerSocket(SERVER_PORT);
        while(true){
            Socket socket = server.accept();
            Client client = new Client(socket);
            new LoginHandler(client).start();
        }
    }
}

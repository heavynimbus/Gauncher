package gauncher;

import static java.lang.String.format;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import javax.net.ServerSocketFactory;

public class Server {

  private static final int SERVER_PORT = 8080;

  public static void main(String[] args) {
    ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
    ServerSocket server = null; // null
    try {
      server = serverSocketFactory.createServerSocket(SERVER_PORT);
    } catch (IOException e) {
      System.out.println(format("Unable to create server at port %s", SERVER_PORT));
      System.exit(1);
    }
    System.out.println(format("Server running at port %s", SERVER_PORT));

    while (true) {
      Socket client = null;
      try {
        client = server.accept();
        System.out.println("New client detected");

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        Optional.ofNullable(client)
            .ifPresent(
                c -> {
                  try {
                    c.close();
                  } catch (IOException ignored) {
                  }
                });
      }
    }
  }
}

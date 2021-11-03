package gauncher;

import static java.lang.String.format;

import gauncher.channel.Channel;
import gauncher.channel.chat.Chat;
import gauncher.handlers.CommandHandler;
import gauncher.logging.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.net.ServerSocketFactory;

public class Server {

  private static final int SERVER_PORT = 8080;

  public static Map<String, ArrayList<Socket>> channels = new HashMap<>();
  public static List<Channel> channelList =
      List.of(new Chat());

  public static Optional<Channel> getChannel(String channelName) {
    return channelList.stream()
        .filter(channel -> !channel.isFull())
        .filter(channel -> channel.getName().equals(channelName))
        .findFirst();
  }

  public static void main(String[] args) {
    Logger log = new Logger("ServerLogger");
    channels.put("chat", new ArrayList<>());
    channels.put("demineur", new ArrayList<>());

    log.info(format("Server starting at port %s...", SERVER_PORT));
    ServerSocket serverSocket = null;
    try {
      serverSocket = ServerSocketFactory.getDefault().createServerSocket(SERVER_PORT);
    } catch (IOException e) {
      log.error(format("Unable to start server at port %s", SERVER_PORT));
      System.exit(1);
    }

    while (!serverSocket.isClosed()) {
      log.info("Currently waiting for connections...");
      Socket client = null;
      try {
        client = serverSocket.accept();
        log.info(format("New connection from %s", client.getRemoteSocketAddress()));
        new CommandHandler(client).start();
      } catch (IOException e) {
        log.error("An I/O error has occurred on waiting for a connection");
      }
    }
  }
}

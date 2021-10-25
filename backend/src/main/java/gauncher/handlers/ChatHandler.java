package gauncher.handlers;

import gauncher.Server;
import gauncher.logging.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

import static java.lang.String.format;

public class ChatHandler extends SimpleHandler {
  private Logger log;

  public ChatHandler(Socket client) throws IOException {
    super(client);
    this.log = new Logger("ChatHandler");
  }

  @Override
  public void run() {
    out.println("Welcome to chat !");
    try {
      String line;
      while ((line = in.readLine()) != null) {
        if ("exit".equalsIgnoreCase(line)) {
          out.println("Goodbye !");
          Server.channels.get("chat").remove(client);
          break;
        }
        String finalLine = line;
        Server.channels.get("chat").stream()
            .filter(socket -> !this.client.equals(socket))
            .map(
                socket -> {
                  try {
                    return new PrintWriter(socket.getOutputStream(), true);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  return null;
                })
            .filter(Objects::nonNull)
            .forEach(
                printer -> {
                  log.debug(printer.toString());
                  printer.println(format("%s: %s", this.client.getRemoteSocketAddress(), finalLine));
                });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

package gauncher.handlers;

import static java.lang.String.format;

import gauncher.Server;
import gauncher.logging.Logger;
import gauncher.player.Player;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class ChatHandler extends SimpleHandler {
  private final Logger log;

  public ChatHandler(Player player) throws IOException {
    super(player);
    this.log = new Logger("ChatHandler");
  }

  @Override
  public void run() {
    log.info(format("%s connected to the chat", this.player.getUsername()));
    out.println("Welcome to chat !");
    try {
      String line;
      while ((line = in.readLine()) != null) {
        if ("exit".equalsIgnoreCase(line)) {
          out.println("Goodbye !");

          Server.channels.get("chat").remove(this.player.getSocket());
          break;
        }
        String finalLine = line;
        Server.getChannel("chat")
            .ifPresent(
                c -> {
                  c.playerStream()
                      .map(Player::getSocket)
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
                            printer.println(format("%s: %s", this.player.getUsername(), finalLine));
                          });
                });
        log.info(format("Message from %s: %s", this.player.getUsername(), finalLine));
        /*Server.channels.get("chat").stream()
        .filter(socket -> !this.player.getSocket().equals(socket))
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
              printer.println(format("%s: %s", this.player.getUsername(), finalLine));
              log.info(format("Message from %s: %s", this.player.getUsername(), finalLine));
            });*/
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

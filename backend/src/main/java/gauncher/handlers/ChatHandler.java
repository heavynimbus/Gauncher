package gauncher.handlers;

import static java.lang.String.format;

import gauncher.Server;
import gauncher.logging.Logger;
import gauncher.player.Player;
import java.io.IOException;
import java.net.SocketException;

public class ChatHandler extends SimpleHandler {
  private final Logger log;

  public ChatHandler(Player player) {
    super(player);
    this.log = new Logger("ChatHandler");
  }

  @Override
  public void run() {
    var joinedTheChatMessage = format("%s joined the chat", player.getUsername());
    log.info(joinedTheChatMessage);
    Server.getChannel("chat").ifPresent(c -> c.sendAll(joinedTheChatMessage));

    try {
      String line;
      while ((line = player.getReader().readLine()) != null) {
        if ("QUIT".equalsIgnoreCase(line)) {
          player.getPrinter().println("Goodbye !");
          Server.getChannel("chat").ifPresent(c -> c.removePlayer(player));
          Server.getChannel("chat")
              .ifPresent(c -> c.sendAll(format("%s left the chat", player.getUsername())));
          break;
        }
        String finalLine = format("%s: %s", this.player.getUsername(), line);
        Server.getChannel("chat").ifPresent(c -> c.sendAll(finalLine, this.player.getUsername()));
        log.info(format("Message from %s: %s", this.player, line));
      }
    }  catch (SocketException e){
      player.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

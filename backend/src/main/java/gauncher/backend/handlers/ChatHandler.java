package gauncher.backend.handlers;

import static java.lang.String.format;

import gauncher.backend.channel.Channels;
import gauncher.backend.logging.Logger;
import gauncher.backend.player.Player;
import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ChatHandler extends SimpleHandler {
  private final Logger log;

  public ChatHandler(Player player, Integer channelId) {
    super(player, channelId);
    this.log = new Logger("ChatHandler");
  }

  @Override
  public void run() {
    var joinedTheChatMessage = format("%s joined the chat", player.getUsername());
    var chat = Channels.get(this.channelId);
    log.info(joinedTheChatMessage);
    chat.sendAll(joinedTheChatMessage);
    // Server.getChannel("chat").ifPresent(c -> c.sendAll(joinedTheChatMessage));

    try {
      String line;
      while ((line = player.getReader().readLine()) != null) {
        if ("QUIT".equalsIgnoreCase(line)) {
          break;
        }
        if (line.startsWith("/")) {
          var command = line.split(" ")[0];
          if (command.equalsIgnoreCase("/list")) {
            var players =
                chat.playerStream().map(Player::getUsername).collect(Collectors.joining("\n"));
            player.getPrinter().println(players);
          } else if (command.equalsIgnoreCase("/quit")) {
            player.getPrinter().println("Goodbye !");
            chat.removePlayer(player);
            chat.sendAll(format("%s left the chat", player.getUsername()));
            log.info("%s left the chat");
            return;
          } else if (command.equalsIgnoreCase("/mp")) {
            var full_command = line.split(" ");
            if (full_command.length <= 2) {
              player.getPrinter().println(format("Invalid command, /mp <username> <message>"));
            } else {
              var username = full_command[1];
              var message =
                  String.join(" ", Arrays.copyOfRange(full_command, 2, full_command.length));
              chat.playerStream()
                  .filter(player1 -> player1.getUsername().equals(username))
                  .findAny()
                  .ifPresentOrElse(
                      player1 -> {
                        player1
                            .getPrinter()
                            .println(format("[PRIVATE MESSAGE] %s: %s", player, message));
                        log.info(
                            format(
                                "%s sent a PRIVATE MESSAGE to %s: %s", player, player1, message));
                      },
                      () -> {
                        player
                            .getPrinter()
                            .println(
                                format("Invalid command, User %s not found in chat", username));
                        log.error(
                            format(
                                "Invalid command from %s - User %s not found in chat",
                                player, username));
                      });
            }

          } else {
            player.getPrinter().println("Invalid command, try /help");
          }
        } else {
          String finalLine = format("%s: %s", this.player.getUsername(), line);
          Channels.get(channelId).sendAll(finalLine);
          log.info(format("Message from %s: %s", this.player, line));
        }
      }
    } catch (SocketException e) {
      log.error(format("%s has been disconnected", player));
      player.disconnect();
      chat.removePlayer(player);
      chat.sendAll(format("%s left the chat", player.getUsername()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

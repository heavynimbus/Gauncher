package gauncher.handlers;

import static java.lang.String.format;

import gauncher.Server;
import gauncher.channel.Channel;
import gauncher.logging.Logger;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandHandler extends SimpleHandler {
  private Logger log;

  public CommandHandler(Socket client) throws IOException {
    super(client);
    this.log = new Logger("CommandHandler");
  }

  private boolean checkCommand(String line, Command command) {
    return line.split(" ")[0]
        .toLowerCase(Locale.ROOT)
        .equals(command.toString().toLowerCase(Locale.ROOT));
  }

  private void enterInChannel(String channelName) {
    try {
      if (Server.channelList.stream().map(Channel::getName).anyMatch(channelName::equals)) {
        var notFullChannels =
            Server.channelList.stream()
                .filter(channel -> !channel.isFull())
                .filter(channel -> channel.getName().equalsIgnoreCase(channelName))
                .collect(Collectors.toList());
        Server.channelList.stream()
            .filter(channel -> !channel.isFull())
            .forEach(System.out::println);
        if (!notFullChannels.isEmpty()) {
          notFullChannels.get(0).addPlayer(player);
          ChatHandler chat = new ChatHandler(this.player);
          chat.start();
          chat.join();
        } else {
          player.getPrinter().println(format("Channel %s is full", channelName));
          log.error(format("Channel %s is full", channelName));
        }
      } else {
        player.getPrinter().println(format("Unknown channel %s", channelName));
        log.debug(format("Unknown channel, cannot enter to %s", channelName));
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Optional<String> getArgument(String line, Command command) {
    String argument = null;
    try {
      argument = line.substring(command.toString().length() + 1);
      if (argument.isBlank()) {
        argument = null;
      }
    } catch (StringIndexOutOfBoundsException ignored) {
    }
    return Optional.ofNullable(argument);
  }

  @Override
  public void run() {
    String line = null;
    
    while (true) {
      try {
        if ((line = player.getReader().readLine()) == null) break;
      } catch (SocketException ignored) {
        log.error(format("%s has been disconnected", this.player.getUsername()));
      } catch (IOException ignored) {
        return;
      }
      log.info(format("Received from %s: %s", this.player.getUsername(), line));
      if (checkCommand(line, Command.LIST)) {
        player
            .getPrinter()
            .println(
                Server.channelList.stream()
                    .map(Channel::toString)
                    .collect(Collectors.joining("\n")));
      } else if (checkCommand(line, Command.ENTER)) {
        var channel = getArgument(line, Command.ENTER);
        channel.ifPresentOrElse(
            this::enterInChannel,
            () -> {
              log.error("Empty channel");
              player
                  .getPrinter()
                  .println("Invalid command - ENTER need the channel name parameter");
            });
      } else if (checkCommand(line, Command.LOGIN)) {
        var username = getArgument(line, Command.LOGIN);
        username.ifPresentOrElse(
            this.player::setUsername,
            () -> {
              var message =
                  format("Invalid command from %s - LOGIN need a username parameter", this.player);
              log.error(message);
              player.getPrinter().println(message);
            });
      } else if (checkCommand(line, Command.QUIT)) {
        player.getPrinter().println("Disconnection");
        player.disconnect();
      } else {
        player.getPrinter().println("Bad command (LOGIN, LIST, ENTER, QUIT)");
        log.error(format("Unknown command received from %s: %s", this.player, line));
      }
    }
  }
}

package gauncher.handlers;

import static java.lang.String.format;

import gauncher.Server;
import gauncher.logging.Logger;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;

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
    // return line.toLowerCase(Locale.ROOT).startsWith(command.toString().toLowerCase(Locale.ROOT));
  }

  @Override
  public void run() {
    try {
      String line;
      while ((line = this.in.readLine()) != null) {
        log.info(format("Received from %s: %s", client.getRemoteSocketAddress(), line));
        if (checkCommand(line, Command.LIST)) {
          String channelList = String.join(", ", Server.channels.keySet());
          out.println(channelList);
        } else if (checkCommand(line, Command.ENTER)) {
          var channel = line.substring(Command.ENTER.toString().length()+1);
          if (Server.channels.containsKey(channel)) {
            Server.channels.get(channel).add(client);
            ChatHandler chat = new ChatHandler(client);
            chat.start();
            chat.join();
          } else {
            out.println(format("Unknown channel %s", channel));
            log.debug(format("Unknown channel, cannot enter to %s", channel));
          }
        } else {
          out.println("Bad command (LIST, ENTER)");
          log.debug(
              format(
                  "Unknown command received from %s: %s", client.getRemoteSocketAddress(), line));
        }
      }
    } catch (SocketException ignored) {
      log.error(format("%s has been disconnected", this.client.getRemoteSocketAddress()));
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}

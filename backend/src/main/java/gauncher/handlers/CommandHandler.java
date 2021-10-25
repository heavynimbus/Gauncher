package gauncher.handlers;

import static java.lang.String.format;

import gauncher.logging.Logger;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class CommandHandler extends SimpleHandler {
  private Logger log;

  public CommandHandler(Socket client) throws IOException {
    super(client);
    this.log = new Logger("CommandHandler");
  }

  @Override
  public void run() {
    String line;
    try {
      while ((line = this.in.readLine()) != null) {
        log.info(format("Received from %s: %s", client.getRemoteSocketAddress(), line));
        out.println(line);
      }

    } catch (SocketException ignored) {
      log.error(format("%s has been disconnected", this.client.getRemoteSocketAddress()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

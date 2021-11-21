package gauncher.frontend.task;

import gauncher.frontend.client.Client;
import java.io.IOException;
import java.net.Socket;
import javafx.concurrent.Task;

public class ConnectionTask extends Task<Client> {
  private final String url;
  private static final int SERVER_PORT = 8080;

  @Override
  protected Client call() throws IOException {
    return new Client().bindSocket(new Socket(url, SERVER_PORT));
  }

  public ConnectionTask(String url) {
    super();
    this.url = url;
  }
}

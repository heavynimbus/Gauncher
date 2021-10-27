package gauncher.player;

import static java.lang.String.format;

import gauncher.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
  private final Logger log = new Logger();
  private static int count = 1;
  private String username;

  private final Socket socket;
  private final PrintWriter printer;
  private final BufferedReader reader;

  public Player(String username, Socket socket) throws IOException {
    this.username = username;
    this.socket = socket;
    this.printer = new PrintWriter(socket.getOutputStream(), true);
    this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public Player(Socket socket) throws IOException {
    this("Anonymous" + count++, socket);
  }

  public Socket getSocket() {
    return this.socket;
  }

  public String getUsername() {
    return this.username;
  }

  public PrintWriter getPrinter() {
    return printer;
  }

  public BufferedReader getReader() {
    return reader;
  }

  public void setUsername(String username) {
    while (username.charAt(0) == ' ') {
      username = username.replaceFirst(" ", "");
    }
    while (username.charAt(username.length() - 1) == ' ') {
      username = username.substring(0, username.length() - 1);
    }
    printer.println(format("New username: %s", username));
    log.info(format("%s change his username to '%s'", this, username));
    this.username = username;
  }

  public void disconnect() {
    try {
      log.info(format("Disconnection from %s", this));
      this.socket.close();
      this.printer.close();
      this.reader.close();
    } catch (IOException e) {
      log.error(format("Error while closing the socket of %s ", this));
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return String.format("%s%s", username, socket.getRemoteSocketAddress());
  }
}

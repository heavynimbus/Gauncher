package gauncher.player;

import java.net.Socket;

public class Player {
  private static int count = 0;
  private String username;
  private Socket socket;

  public Player(String username, Socket socket) {
    this.username = username;
    this.socket = socket;
  }

  public Player(Socket socket) {
    this("Anonymous" + count++, socket);
  }

  public Socket getSocket() {
    return this.socket;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "Player{" +
        "username='" + username + '\'' +
        ", socket=" + socket +
        '}';
  }
}

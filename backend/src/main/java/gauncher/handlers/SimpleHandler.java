package gauncher.handlers;

import gauncher.player.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SimpleHandler extends Thread {
  protected Player player;
  protected PrintWriter out;
  protected BufferedReader in;

  public SimpleHandler(Player player) throws IOException {
    this.player = player;
    this.out = new PrintWriter(player.getSocket().getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
  }

  public SimpleHandler(Socket client) throws IOException {
    this(new Player(client));
  }
}

package gauncher.handlers;

import gauncher.player.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SimpleHandler extends Thread {
  protected Player player;

  public SimpleHandler(Player player){
    this.player = player;
  }

  public SimpleHandler(Socket client) throws IOException {
    this(new Player(client));
  }
}

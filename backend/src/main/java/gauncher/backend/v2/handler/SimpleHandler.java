package gauncher.backend.v2.handler;

import gauncher.backend.v2.player.Player;

public abstract class SimpleHandler extends Thread {
  protected final Player player;

  public SimpleHandler(Player player) {
    this.player = player;
  }
}

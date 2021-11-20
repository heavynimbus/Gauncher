package gauncher.backend.handlers;

import gauncher.backend.player.Player;

public abstract class SimpleHandler extends Thread {
  protected Player player;
  protected final int channelId;

  public SimpleHandler(Player player, Integer channelId) {
    this.player = player;
    this.channelId = channelId;
  }
}

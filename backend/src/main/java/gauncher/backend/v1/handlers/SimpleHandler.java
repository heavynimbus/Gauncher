package gauncher.backend.v1.handlers;

import gauncher.backend.v1.player.Player;

public abstract class SimpleHandler extends Thread {
  protected Player player;
  protected final int channelId;

  public SimpleHandler(Player player, Integer channelId) {
    this.player = player;
    this.channelId = channelId;
  }
}

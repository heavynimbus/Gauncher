package gauncher.backend.v2.handler;

import gauncher.backend.v2.player.Player;

public abstract class SimpleHandler extends Thread {
  protected Player player;
  protected final int channelId;

  public SimpleHandler(Player player, Integer channelId) {
    this.player = player;
    this.channelId = channelId;
  }
}

package gauncher.backend.channel;

import static java.lang.String.format;

import gauncher.backend.handlers.SimpleHandler;
import gauncher.backend.player.Player;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Channel {
  private static int idCount = 0;
  private int id;
  protected boolean full;
  protected final int limit;
  protected ArrayList<Player> players;
  protected final Class<? extends SimpleHandler> handler;
  protected final String name;
  protected final String description;
  protected ChannelStatus status;

  public Channel(
      String name, int limit, String description, Class<? extends SimpleHandler> handler) {
    this.id = idCount++;
    this.handler = handler;
    this.name = name;
    this.full = false;
    this.limit = limit;
    this.description = description;
    this.players = new ArrayList<>();
  }

  public boolean isFull() {
    // if (!full) return this.limit > 0 && this.players.size() >= this.limit;
    return full;
  }

  public int getLimit() {
    return limit;
  }

  public int getSize() {
    return players.size();
  }

  public void setFull(boolean full) {
    this.full = full;
  }

  public SimpleHandler addPlayer(Player p) {
    try {
      players.add(p);
      SimpleHandler simplehandler =
          handler.getDeclaredConstructor(Player.class, Integer.class).newInstance(p, id);
      simplehandler.start();
      if (players.size() >= limit && limit > 0) {
        setFull(true);
        Channels.addChannel(this.getClass());
      }
      return simplehandler;
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | InstantiationException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void removePlayer(Player player) {
    this.players.remove(player);
  }

  public Stream<Player> playerStream() {
    return this.players.stream();
  }

  public void sendAll(String message, String... except) {
    this.playerStream()
        .filter(player -> !Arrays.asList(except).contains(player.getUsername()))
        .forEach(player -> player.getPrinter().println(message));
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public ChannelStatus status() {
    return status;
  }

  public void setStatus(ChannelStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return format("%s(%d)\t%s/%s\t%s", this.getName(), id, this.getSize(), limit, description);
  }
}

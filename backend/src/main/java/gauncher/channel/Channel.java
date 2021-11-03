package gauncher.channel;

import static java.lang.String.format;

import gauncher.handlers.SimpleHandler;
import gauncher.player.Player;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Channel {
  protected boolean inProgress;
  protected final int limit;
  protected ArrayList<Player> players;
  protected final Class<? extends SimpleHandler> handler;
  protected final String name;
  protected final String description;

  public Channel(
      String name, int limit, String description, Class<? extends SimpleHandler> handler) {
    this.handler = handler;
    this.name = name;
    this.inProgress = false;
    this.limit = limit;
    this.description = description;
    this.players = new ArrayList<>();
  }

  public boolean isInProgress() {
    return inProgress;
  }

  public boolean isFull() {
    return this.limit > 0 && this.players.size() >= this.limit;
  }

  public void addPlayer(Player p) {
    players.add(p);
    try {
      SimpleHandler simplehandler = handler.getDeclaredConstructor(Player.class).newInstance(p);
      simplehandler.start();
      simplehandler.join();
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | InstantiationException
        | InterruptedException e) {
      e.printStackTrace();
    }
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

  @Override
  public String toString() {
    return format("%s\t%s\t%s", this.getName(), limit > 0 ? limit : "no limit", description);
  }
}

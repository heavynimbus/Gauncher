package gauncher.channel;

import static java.lang.String.format;

import gauncher.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Channel {
  private boolean inProgress;
  private String name;
  private int limit;
  private ArrayList<Player> players;

  private String description;

  public Channel(String name, int limit, String description) {
    this.inProgress = false;
    this.name = name;
    this.limit = limit;
    this.description = description;
    this.players = new ArrayList<>();
  }

  public boolean isInProgress() {
    return inProgress;
  }

  public String getName() {
    return this.name;
  }

  public boolean isFull() {
    return this.limit > 0 && this.players.size() >= this.limit;
  }

  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public void removePlayer(Player player){
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

  @Override
  public String toString() {
    return format("%s\t%s\t%s", name, limit > 0 ? limit : "no limit", description);
  }
}

package gauncher.channel;

import static java.lang.String.format;

import gauncher.player.Player;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Channel {
  
  private String name;
  private int limit;
  private ArrayList<Player> players;
  private String description;

  public Channel(String name, int limit, String description) {
    this.name = name;
    this.limit = limit;
    this.description = description;
    this.players = new ArrayList<>();
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

  public Stream<Player> playerStream() {
    return this.players.stream();
  }

  @Override
  public String toString() {
    return format("%s\t%s\t%s", name, limit > 0 ? limit : "no limit", description);
  }
}

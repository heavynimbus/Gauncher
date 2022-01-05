package gauncher.backend.v1.channel;

import gauncher.backend.v1.channel.chat.ChatChannel;
import gauncher.backend.v1.channel.chess.ChessChannel;
import gauncher.backend.v1.handlers.SimpleHandler;
import gauncher.backend.v1.logging.Logger;
import gauncher.backend.v1.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

public class Channels {
  private static final Logger log = new Logger("Channels");
  public static List<Channel> channelList;
  private static CommandChannel commandChannel;

  public static void initChannelList() {
    channelList = new ArrayList<>();
    addChannel(ChatChannel.class);
    addChannel(ChessChannel.class);
    commandChannel = new CommandChannel();
  }

  public static List<Channel> sort() {
    channelList.sort(
        (channel1, channel2) -> {
          if (channel1.getId() == channel2.getId()) return 0;
          if (channel1.getClass().equals(channel2.getClass()))
            return channel1.getId() - channel2.getId();
          return channel1.getClass().toString().compareTo(channel2.getClass().toString());
        });
    return channelList;
  }

  public static void addChannel(Class<? extends Channel> channelClass) {
    try {
      channelList.add(channelClass.getDeclaredConstructor().newInstance());
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  public static Channel get(int channelId) {
    return channelList.stream()
        .filter(channel -> channel.getId() == channelId)
        .findAny()
        .orElseThrow();
  }

  public static SimpleHandler addPlayer(Class<? extends Channel> channelClass, Player player) {
    if (channelClass.equals(CommandChannel.class)) {
      return commandChannel.addPlayer(player);
    }
    AtomicReference<SimpleHandler> result = new AtomicReference<>();
    Channels.sort().parallelStream()
        .filter(channel -> !channel.isFull())
        .filter(channel -> channel.getClass().equals(channelClass))
        .findFirst()
        .ifPresent(channel -> result.set(channel.addPlayer(player)));
    return result.get();
  }

  public static boolean checkUsername(String username) {
    var list = new ArrayList<>(List.copyOf(channelList));
    list.add(commandChannel);
    return list.stream()
        .flatMap((Function<Channel, Stream<?>>) Channel::playerStream)
        .map(elt -> (Player) elt)
        .map(Player::getUsername)
        .filter(username::equals)
        .findAny()
        .isEmpty();
  }
}

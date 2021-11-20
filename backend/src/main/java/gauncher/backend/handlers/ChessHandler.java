package gauncher.backend.handlers;

import static java.lang.String.format;

import gauncher.backend.channel.Channels;
import gauncher.backend.channel.chess.ChessChannel;
import gauncher.backend.channel.chess.pawn.PieceColor;
import gauncher.backend.logging.Logger;
import gauncher.backend.player.Player;
import java.io.IOException;
import java.net.SocketException;

public class ChessHandler extends SimpleHandler {
  private final Logger log;
  private final PieceColor color;

  public ChessHandler(Player player, Integer channelId) {
    super(player, channelId);
    this.color = Channels.get(channelId).getSize() == 2 ? PieceColor.BLACK : PieceColor.WHITE;
    this.log = new Logger("ChessHandler");
  }

  @Override
  public void run() {
    ChessChannel chessChannel = (ChessChannel) Channels.get(channelId);
    log.info(format("%s is connected to Chess game with id %d", player, channelId));
    player.getPrinter().println(format("Your color: %s", color));
    String line = null;
    try {
      while ((line = player.getReader().readLine()) != null) {
        if (!chessChannel.isFull()) {
          player.getPrinter().println("Waiting for players...");
        } else {
          player.getPrinter().println("play");
        }
      }
    } catch (SocketException e) {
      player.disconnect();
      chessChannel.removePlayer(player);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

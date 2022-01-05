package gauncher.backend.v1.channel.chess.pawn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class King extends Piece {

  public King(PieceColor color, int line, int column) {
    super(color, "\u2654", line, column, getInitialMoves());
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265A";
    }
  }

  private static List<int[]> getInitialMoves() {
    return List.of(
        new int[]{1, -1},
        new int[]{1, 0},
        new int[]{1, 1},
        new int[]{0, -1},
        new int[]{0, 1},
        new int[]{-1, -1},
        new int[]{-1, 0},
        new int[]{-1, 1}
    );
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    return initialMoves.stream().filter(move -> {
      if (!checkIndex(move[0], move[1])) {
        return false;
      }
      Optional<Piece> piece = board[line + move[0]][column + move[1]];
      return piece.filter(value -> !value.color.equals(color)).isPresent();
    }).map(move -> new int[]{move[0] + line, move[1] + column}).collect(Collectors.toList());
  }
}

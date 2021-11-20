package gauncher.backend.channel.chess.pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Bishop extends Piece {

  public Bishop(PieceColor color, int line, int column) {
    super(color, "\u2657", line, column, getInitialMoves());
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265D";
    }
  }

  private static List<int[]> getInitialMoves() {
    List<int[]> initialMoves = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      if (i != 0) {
        initialMoves.add(new int[]{i, i});
        initialMoves.add(new int[]{i, -i});
        initialMoves.add(new int[]{-i, i});
        initialMoves.add(new int[]{-i, -i});
      }
    }
    initialMoves.sort((move1, move2) -> {
      int a = move1[0], b = move1[1], c = move2[0], d = move2[1];
      if (a == c && b == d) {
        return 1;
      }
      if ((a > 0 && c > 0) || (a < 0 && c < 0)) {
        if ((b > 0 && d > 0) || (b < 0 && d < 0)) {
          return a < 0 ? c - a : a - c;
        } else if (b > 0 && d < 0) {
          return 1;
        } else if (b < 0 && d > 0) {
          return -1;
        }
      } else if (a > 0 && c < 0) {
        return -1;
      } else if (a < 0 && c > 0) {
        return 1;
      }
      return -1;
    });
    return initialMoves;
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    AtomicBoolean canMove = new AtomicBoolean(true);
    return initialMoves.stream().filter(move -> {
      if (move[0] == 1 || move[0] == -1) {
        canMove.set(true);
      }
      if (!checkIndex(move[0], move[1]) || !canMove.get()) {
        return false;
      }
      Optional<Piece> piece = board[line + move[0]][column + move[1]];
      if (piece.isPresent()) {
        if (!piece.get().color.equals(color)) {
          canMove.set(false);
          return true;
        } else {
          canMove.set(false);
          return false;
        }
      }
      return true;
    }).map(move -> new int[]{move[0] + line, move[1] + column})
        .collect(Collectors.toList());
  }
}

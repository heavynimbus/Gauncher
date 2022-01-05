package gauncher.backend.v1.channel.chess.pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Rook extends Piece {

  public Rook(PieceColor color, int line, int column) {
    super(color, "\u2656", line, column, getInitialMoves());
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265C";
    }
  }

  private static List<int[]> getInitialMoves() {
    List<int[]> res = new ArrayList<>();
    for (int i = 1; i < 8; i++) {
      res.add(new int[]{i, 0});
      res.add(new int[]{-i, 0});
      res.add(new int[]{0, i});
      res.add(new int[]{0, -i});
    }
    res.sort((move1, move2) -> {
      int a = move1[0], b = move1[1], c = move2[0], d = move2[1];
      if (a == 0 && c != 0) {
        return -1;
      } else if (b == 0 && d != 0) {
        return 1;
      } else if (a == 0 || b == 0) {
        int val1 = a == 0 ? d : a;
        int val2 = a == 0 ? b : c;
        if (val1 < 0 && val2 > 0) {
          return 1;
        } else if (val1 > 0 && val2 < 0) {
          return -1;
        } else if (val1 > 0 && val2 > 0) {
          return b - d;
        }
        return d - b;
      }
      return 0;
    });
    return res;
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    AtomicBoolean canMove = new AtomicBoolean(false);
    return initialMoves.stream()
        .filter(move -> {
          if (move[0] == 1 || move[0] == -1 || move[1] == 1 || move[1] == -1) {
            canMove.set(true);
          }
          return true;
        })
        .filter(move -> checkIndex(move[0], move[1]))
        .filter(move -> canMove.get())
        .filter(move -> {
          Optional<Piece> piece = board[line + move[0]][column + move[1]];
          if (piece.isPresent()) {
            if (piece.get().color.equals(color)) {
              canMove.set(false);
              return false;
            } else {
              canMove.set(false);
              return true;
            }
          }
          return true;
        })
        .collect(Collectors.toList());
  }
}

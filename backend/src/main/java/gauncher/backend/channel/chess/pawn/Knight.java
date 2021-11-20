package gauncher.backend.channel.chess.pawn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Knight extends Piece {

  public Knight(PieceColor color, int line, int column) {
    super(color, "\u2658", line, column, getInitialMoves());
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265E";
    }
  }

  private static List<int[]> getInitialMoves() {
    return List.of(
        new int[]{2, 1},
        new int[]{2, -1},
        new int[]{-2, 1},
        new int[]{-2, -1},
        new int[]{1, 2},
        new int[]{1, -2},
        new int[]{-1, 2},
        new int[]{-1, -2}
    );
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    return initialMoves.stream().filter(move ->
        line + move[0] >= 0 && line + move[0] < board.length && column + move[1] >= 0
            && column + move[1] < board[0].length
    ).filter(move ->
        board[line + move[0]][column + move[1]].filter(this::isSameColor).isEmpty()
    ).map(move -> new int[]{move[0] + line, move[1] + column})
        .collect(Collectors.toList());
  }
}

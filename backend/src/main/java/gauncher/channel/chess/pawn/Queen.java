package gauncher.channel.chess.pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Queen extends Piece {

  public Queen(PieceColor color, int line, int column) {
    super(color, "\u2655", line, column, null);
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265B";
    }
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    List<int[]> possibleMoves = new ArrayList<>();
    boolean[] validDirection = new boolean[]{true, true, true, true, true, true, true, true};
    for (int i = 1; i < 8; i++) {
      var mooves = new int[][]{{i, 0}, {i, i}, {0, i}, {-i, i}, {-i, 0}, {-i, -i}, {0, -i},
          {i, -i}};
      for (int j = 0; j < mooves.length; j++) {
        var move = mooves[j];
        if (validDirection[j] && checkIndex(move[0], move[1])) {
          Optional<Piece> piece = board[line + move[0]][column + move[1]];
          int finalJ = j;
          piece.ifPresentOrElse(p -> {
                if (!p.color.equals(color)) {
                  possibleMoves.add(move);
                }
                validDirection[finalJ] = false;
              }, () ->
                  possibleMoves.add(move)
          );
        }
      }
    }
    return possibleMoves
        .stream()
        .map(move -> new int[]{move[0] + line, move[1] + column})
        .collect(Collectors.toList());
  }
}

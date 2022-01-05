package gauncher.backend.v1.channel.chess.pawn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Pawn extends Piece {

  private boolean hasMoved;

  public Pawn(PieceColor color, int line, int column) {
    super(color, "\u2659", line, column, getInitialMoves(color));
    this.hasMoved = false;
    if (color.equals(PieceColor.BLACK)) {
      this.unicodeValue = "\u265F";
    }
  }

  private static List<int[]> getInitialMoves(PieceColor color) {
    if (color.equals(PieceColor.BLACK)) {
      return List.of(new int[]{-1, 0}, new int[]{-2, 0}, new int[]{-1, -1}, new int[]{-1, 1});
    } else {
      return List.of(new int[]{1, 0}, new int[]{2, 0}, new int[]{1, 1}, new int[]{1, -1});
    }
  }

  @Override
  public List<int[]> getMoves(Optional<Piece>[][] board) {
    var res = initialMoves.stream().filter(move -> {
      if (!checkIndex(move[0], move[1])) {
        return false;
      }
      if (!hasMoved && (move[0] == 2 || move[0] == -2)) {
        var previous = board[line + (move[0] == 2 ? 1 : -1)][column];
        if (previous.isPresent()) {
          return false;
        }
      }
      Optional<Piece> piece = board[line + move[0]][column + move[1]];
      if (move[1] == 0) {
        return piece.isEmpty();
      } else {
        return piece.filter(value -> !value.color.equals(color)).isPresent();
      }
    }).map(move -> new int[]{move[0] + line, move[1] + column}).collect(Collectors.toList());
    if (!hasMoved) {
      if (color.equals(PieceColor.BLACK)) {
        this.initialMoves = List.of(new int[]{-1, 0}, new int[]{-1, -1}, new int[]{-1, 1});
      } else {
        this.initialMoves = List.of(new int[]{1, 0}, new int[]{1, 1}, new int[]{1, -1});
      }
    }
    return res;
  }
}

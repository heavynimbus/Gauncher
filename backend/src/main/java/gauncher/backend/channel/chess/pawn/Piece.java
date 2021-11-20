package gauncher.backend.channel.chess.pawn;

import java.util.List;
import java.util.Optional;

public abstract class Piece {

  protected List<int[]> initialMoves;
  protected PieceColor color;
  protected String unicodeValue;
  protected int line;
  protected int column;

  public Piece(PieceColor color, String unicodeValue, int line, int column,
      List<int[]> initialMoves) {
    this.color = color;
    this.unicodeValue = unicodeValue;
    this.line = line;
    this.column = column;
    this.initialMoves = initialMoves;
  }

  public String getUnicodeValue() {
    return this.unicodeValue;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public boolean isSameColor(Piece otherPiece) {
    return this.color.equals(otherPiece.color);
  }

  public abstract List<int[]> getMoves(Optional<Piece>[][] board);

  protected boolean checkIndex(int line, int column) {
    return this.line + line >= 0 && this.line + line < 8 && this.column + column >= 0
        && this.column + column < 8;
  }

  @Override
  public String toString() {
    return "Piece{" +
        unicodeValue +
        ", line=" + line +
        ", column=" + column +
        '}';
  }
}

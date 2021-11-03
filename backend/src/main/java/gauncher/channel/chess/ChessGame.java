package gauncher.channel.chess;

import com.heavynimbus.chess.exception.InvalidMoveException;
import com.heavynimbus.chess.exception.PieceNotFoundException;
import gauncher.channel.Channel;
import gauncher.channel.chess.pawn.Bishop;
import gauncher.channel.chess.pawn.King;
import gauncher.channel.chess.pawn.Knight;
import gauncher.channel.chess.pawn.Pawn;
import gauncher.channel.chess.pawn.Piece;
import gauncher.channel.chess.pawn.PieceColor;
import gauncher.channel.chess.pawn.Queen;
import gauncher.channel.chess.pawn.Rook;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessGame extends Channel {

  public Optional<Piece>[][] board;

  public ChessGame() {
    super("chess", 2, "A chess game (2 players)", null);
    this.initBoard();
  }


  public Optional<Piece> getAt(int line, int column) {
    return board[line][column];
  }

  private Optional<Piece> getPieceWithIndex(int i, int j, PieceColor color) {
    if (j == 0 || j == 7) {
      return Optional.of(new Rook(color, i, j));
    } else if (j == 1 || j == 6) {
      return Optional.of(new Knight(color, i, j));
    } else if (j == 2 || j == 5) {
      return Optional.of(new Bishop(color, i, j));
    } else if (j == 3) {
      return Optional.of(new Queen(color, i, j));
    } else {
      return Optional.of(new King(color, i, j));
    }
  }

  private void initBoard() {
    board = new Optional[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Optional<Piece> value = Optional.empty();
        if (i == 0) { // white side
          value = this.getPieceWithIndex(i, j, PieceColor.WHITE);
        } else if (i == 1) { // white pawns
          value = Optional.of(new Pawn(PieceColor.WHITE, i, j));
        } else if (i == 6) { // black pawns
          value = Optional.of(new Pawn(PieceColor.BLACK, i, j));
        } else if (i == 7) { // black side
          value = this.getPieceWithIndex(i, j, PieceColor.BLACK);
        }
        board[i][j] = value;
      }
    }
  }

  public Optional<Piece>[][] getBoard() {
    return board;
  }

  public String toStringWithPossibleMoves(List<int[]> possibleMoves) {
    StringBuilder builder = new StringBuilder();
    builder.append("\t|\tA\t\t|\tB\t\t|\tC\t\t|\tD\t\t|\tE\t\t|\tF\t\t|\tG\t\t|\tH\t\n");
    for (int i = 0; i < board.length; i++) {
      Optional<Piece>[] optionals = board[i];
      for (int j = 0; j < optionals.length; j++) {
        Optional<Piece> optionalPiece = optionals[j];
        builder.append("\t|\t");
        int finalI = i, finalJ = j;
        String str =
            possibleMoves.stream().anyMatch(move -> move[0] == finalI && move[1] == finalJ)
                ? "x"
                : "";
        builder.append(str);
        optionalPiece.ifPresentOrElse(
            piece -> builder.append(piece.getUnicodeValue()), () -> builder.append("\t"));
      }
      builder.append('\n');
    }
    return builder.toString();
  }

  public boolean movePiece(int fromLine, int fromColumn, int toLine, int toColumn)
      throws PieceNotFoundException, InvalidMoveException {
    Piece piece1 = board[fromLine][fromColumn].orElseThrow(PieceNotFoundException::new);
    if (piece1.getMoves(board).stream()
        .noneMatch(move -> move[0] == toLine && move[1] == toColumn)) {
      throw new InvalidMoveException();
    }
    Optional<Piece> piece2 = board[toLine][toColumn];
    piece2.ifPresentOrElse(
        piece -> {
          piece1.setLine(toLine);
          piece1.setColumn(toColumn);
          board[toLine][toColumn] = Optional.of(piece1);
          piece.setLine(fromLine);
          piece.setColumn(fromColumn);
          board[fromLine][fromColumn] = Optional.of(piece);
        },
        () -> {
          piece1.setLine(toLine);
          piece1.setColumn(toColumn);
          board[toLine][toColumn] = Optional.of(piece1);
          board[fromLine][fromColumn] = Optional.empty();
        });
    return true;
  }



  @Override
  public String toString() {
    return toStringWithPossibleMoves(new ArrayList<>());
  }
}

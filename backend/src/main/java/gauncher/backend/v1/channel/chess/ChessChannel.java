package gauncher.backend.v1.channel.chess;

import gauncher.backend.v1.channel.chess.exception.InvalidMoveException;
import gauncher.backend.v1.channel.chess.exception.PieceNotFoundException;
import gauncher.backend.v1.channel.Channel;
import gauncher.backend.v1.channel.chess.pawn.Bishop;
import gauncher.backend.v1.channel.chess.pawn.King;
import gauncher.backend.v1.channel.chess.pawn.Knight;
import gauncher.backend.v1.channel.chess.pawn.Pawn;
import gauncher.backend.v1.channel.chess.pawn.Piece;
import gauncher.backend.v1.channel.chess.pawn.PieceColor;
import gauncher.backend.v1.channel.chess.pawn.Queen;
import gauncher.backend.v1.channel.chess.pawn.Rook;
import gauncher.backend.v1.handlers.ChessHandler;

import java.util.List;
import java.util.Optional;

public class ChessChannel extends Channel {

  private Optional<Piece>[][] board;
  private PieceColor colorWhichIsPlaying;

  public ChessChannel() {
    super("chess", 2, "A chess game (2 players)", ChessHandler.class);
    this.initBoard();
    this.colorWhichIsPlaying = PieceColor.WHITE;
  }

  public PieceColor getColorWhichIsPlaying() {
    return colorWhichIsPlaying;
  }

  public void nextPlayer() {
    this.colorWhichIsPlaying =
        this.colorWhichIsPlaying == PieceColor.BLACK ? PieceColor.BLACK : PieceColor.WHITE;
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
}

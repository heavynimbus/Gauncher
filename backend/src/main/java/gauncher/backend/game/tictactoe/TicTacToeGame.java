package gauncher.backend.game.tictactoe;


import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.game.Game;
import gauncher.backend.handler.MenuHandler;
import gauncher.backend.handler.TicTacToeHandler;
import gauncher.backend.logging.Logger;

import java.util.*;

import static gauncher.backend.game.tictactoe.TicTacToeType.*;


public class TicTacToeGame extends Game {
    private final static Logger log = new Logger("TicTacToeGame");
    private final Map<TicTacToeType, ClientEntity> players;
    private boolean isReady;
    private final TicTacToeType[][] board;
    private TicTacToeType currentPlayer;
    private final List<TicTacToeHandler> handlers;

    public TicTacToeGame() {
        super("tictactoe", 2, TicTacToeHandler.class);
        this.players = new HashMap<>();
        this.isReady = false;
        this.board = new TicTacToeType[3][3];
        this.initBoard();
        this.currentPlayer = NONE;
        this.handlers = new ArrayList<>();
    }

    public void setCurrentPlayer() {
        switch (currentPlayer) {
            case CIRCLE:
                currentPlayer = CROSS;
                break;
            case CROSS:
            case NONE:
                currentPlayer = CIRCLE;
        }
    }

    public TicTacToeType getCurrentPlayerType() {
        return currentPlayer;
    }

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = TicTacToeType.NONE;
            }
        }
    }

    public Map<TicTacToeType, ClientEntity> getPlayers() {
        return players;
    }

    public void addPlayer(ClientEntity client) {
        if (this.isReady) return;
        if (this.players.containsKey(TicTacToeType.CROSS)) {
            players.put(TicTacToeType.CIRCLE, client);
            isReady = true;
            setCurrentPlayer();
            return;
        }
        players.put(CROSS, client);
    }

    public void addHandler(TicTacToeHandler handler) {
        this.handlers.add(handler);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (TicTacToeType[] ticTacToeTypes : board) {
            for (int j = 0; j < board[0].length; j++) {
                res.append(ticTacToeTypes[j].getValue());
            }
        }
        return res.toString();
    }


    public String play(String line) {
        var okPrefix = "OK ";
        if (line.startsWith(okPrefix)) {
            line = line.substring(okPrefix.length());
            if (!checkBoard(line)) {
                return "KO invalid board format";
            }
            setBoard(line);
            var winner = checkEndGame();
            if (winner != NONE) {
                endGame(players.get(winner));
                return "END"; //String.format("END", players.get(winner));
            }
            setCurrentPlayer();
            return String.format("OK Time to %s", currentPlayer);
        } else {
            return "KO Your response have to be like 'OK {board}' or 'WHERE'";
        }
    }

    @Override
    public void endGame(ClientEntity winner) {
        super.endGame(winner);
        handlers.forEach(handler -> {
            new MenuHandler(handler.getClientEntity()).start();
            System.out.println("Interrupt " + handler);
            handler.interrupt();
        });
    }

    public boolean checkBoard(String board) {
        if (board.length() != 9) {
            log.error("Invalid board, length is wrong, have to be 9");
            return false;
        }
        return board.chars()
                .mapToObj(i -> (char) i)
                .map(Object::toString)
                .filter(TicTacToeType::isValue)
                .count() == 9;
    }

    public void setBoard(String boardString) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                var c = String.valueOf(boardString.charAt(i * board.length + j));
                board[i][j] = TicTacToeType.getValue(c);
            }
        }
    }

    // returns true if the game is ended
    public TicTacToeType checkEndGame() {
        TicTacToeType winLines = checkLines(),
                winColumns = checkColumns(),
                winDiags = checkDiagonals();
        if (winLines != NONE) return winLines;
        if (winColumns != NONE) return winColumns;
        return winDiags;
    }

    private TicTacToeType checkLines() {
        for (TicTacToeType[] ticTacToeTypes : board) {
            if (Arrays.stream(ticTacToeTypes).allMatch(CROSS::equals)) return CROSS;
            else if (Arrays.stream(ticTacToeTypes).allMatch(CIRCLE::equals)) return CIRCLE;
        }
        return NONE;
    }

    private TicTacToeType checkColumns() {
        for (int j = 0; j < board[0].length; j++) {
            TicTacToeType[] array = new TicTacToeType[board.length];
            for (int i = 0; i < board.length; i++) {
                array[i] = board[i][j];
            }
            if (Arrays.stream(array).allMatch(CROSS::equals)) return CROSS;
            else if (Arrays.stream(array).allMatch(CIRCLE::equals)) return CIRCLE;
        }
        return NONE;
    }

    private TicTacToeType checkDiagonals() {
        TicTacToeType[] firstDiag = new TicTacToeType[3], secondDiag = new TicTacToeType[3];
        for (int i = 0; i < board.length; i++) {
            firstDiag[i] = board[i][i];
            secondDiag[i] = board[board.length - 1 - i][i];
        }
        if (Arrays.stream(firstDiag).allMatch(CROSS::equals)) return CROSS;
        else if (Arrays.stream(firstDiag).allMatch(CIRCLE::equals)) return CIRCLE;
        else if (Arrays.stream(secondDiag).allMatch(CROSS::equals)) return CROSS;
        else if (Arrays.stream(secondDiag).allMatch(CIRCLE::equals)) return CIRCLE;
        return NONE;
    }

    @Override
    public Game copy() {
        return new TicTacToeGame();
    }
}

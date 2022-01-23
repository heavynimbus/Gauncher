package gauncher.backend.game.tictactoe;


import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.game.Game;
import gauncher.backend.handler.TicTacToeHandler;

import java.util.HashMap;
import java.util.Map;

import static gauncher.backend.game.tictactoe.TicTacToeType.*;


public class TicTacToeGame extends Game {
    private final Map<TicTacToeType, ClientEntity> players;
    private boolean isReady;
    private final TicTacToeType[][] board;
    private TicTacToeType currentPlayer;

    public TicTacToeGame() {
        super("tictactoe", 2, TicTacToeHandler.class);
        this.players = new HashMap<>();
        this.isReady = false;
        this.board = new TicTacToeType[3][3];
        this.initBoard();
        this.currentPlayer = NONE;
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

    public TicTacToeType addPlayer(ClientEntity client) {
        if (this.isReady) return TicTacToeType.NONE;
        if (this.players.containsKey(TicTacToeType.CROSS)) {
            players.put(TicTacToeType.CIRCLE, client);
            isReady = true;
            setCurrentPlayer();
            return TicTacToeType.CIRCLE;
        }
        players.put(CROSS, client);
        return TicTacToeType.CROSS;
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
        if (line.startsWith("OK")) {
            setCurrentPlayer();
            return null;
        } else {
            return "KO Your response have to be like 'OK {board}'";
        }
    }
}

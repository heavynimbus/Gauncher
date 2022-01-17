package gauncher.backend.game.tictactoe;

import static gauncher.backend.game.tictactoe.TicTacToeType.CROSS;


import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.game.Game;
import gauncher.backend.handler.TicTacToeHandler;

import java.util.HashMap;
import java.util.Map;


public class TicTacToeGame extends Game {
    private final Map<TicTacToeType, ClientEntity> players;
    private boolean isReady;
    private final TicTacToeType[][] board;

    public TicTacToeGame() {
        super("tictactoe", 2, TicTacToeHandler.class);
        this.players = new HashMap<>();
        this.isReady = false;
        this.board = new TicTacToeType[3][3];
        this.initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = TicTacToeType.NONE;
            }
        }
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public TicTacToeType addPlayer(ClientEntity client){
        if(this.isReady) return TicTacToeType.NONE;
        if (this.players.containsKey(TicTacToeType.CROSS)){
            players.put(TicTacToeType.CIRCLE, client);
            isReady = true;
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
}

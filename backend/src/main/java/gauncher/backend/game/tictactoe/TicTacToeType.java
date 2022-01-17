package gauncher.backend.game.tictactoe;

public enum TicTacToeType {
    NONE("."), CROSS("X"), CIRCLE("O");

    private final String value;

    TicTacToeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

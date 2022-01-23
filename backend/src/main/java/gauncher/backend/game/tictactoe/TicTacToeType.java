package gauncher.backend.game.tictactoe;

import gauncher.backend.logging.Logger;

import java.util.Arrays;

public enum TicTacToeType {
    NONE("."), CROSS("X"), CIRCLE("O");
    private final static Logger log = new Logger("TicTacToeType");
    private final String value;

    TicTacToeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValue(String value) {
        var optionalValue = Arrays.stream(TicTacToeType.values())
                .map(TicTacToeType::getValue)
                .filter(value::equals).findAny();
        if (optionalValue.isEmpty()) {
            log.error("%s is not a TicTacToeType", value);
        }
        return optionalValue.isPresent();
    }

    public static TicTacToeType getValue(String value) {
        return Arrays.stream(TicTacToeType.values())
                .filter(type -> type.value.equals(value))
                .findAny().orElseThrow();
    }
}

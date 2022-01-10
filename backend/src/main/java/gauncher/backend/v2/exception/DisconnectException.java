package gauncher.backend.v2.exception;

import gauncher.backend.v2.player.Player;

public class DisconnectException extends Exception {
    public DisconnectException(Player player) {
        super(String.format("%s has been disconnected", player));
        player.disconnect();
    }
}

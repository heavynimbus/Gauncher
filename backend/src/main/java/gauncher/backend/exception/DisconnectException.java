package gauncher.backend.exception;

import gauncher.backend.database.entity.Client;

public class DisconnectException extends Exception {
    public DisconnectException(Client client) {
        super(String.format("%s has been disconnected", client));
    }
}

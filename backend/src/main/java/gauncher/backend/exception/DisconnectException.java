package gauncher.backend.exception;

import gauncher.backend.database.entity.ClientEntity;

public class DisconnectException extends Exception {
    public DisconnectException(ClientEntity clientEntity) {
        super(String.format("%s has been disconnected", clientEntity));
    }
}

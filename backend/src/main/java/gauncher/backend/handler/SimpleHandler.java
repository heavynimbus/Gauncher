package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;

public abstract class SimpleHandler extends Thread {
    protected ClientEntity clientEntity;
    public SimpleHandler(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }
}

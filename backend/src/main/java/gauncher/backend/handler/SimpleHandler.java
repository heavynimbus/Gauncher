package gauncher.backend.handler;

import gauncher.backend.database.entity.Client;

public abstract class SimpleHandler extends Thread {
    protected final Client client;

    public SimpleHandler(Client client) {
        this.client = client;
    }
}

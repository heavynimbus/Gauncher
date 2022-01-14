package gauncher.backend.game;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.handler.SimpleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private static int gameIdCount = 1;

    private final int id;
    private final String name;
    private final int limit;
    private final List<ClientEntity> clients;
    private boolean isOpen;
    private Class<? extends SimpleHandler> handler;

    public Game(String name, int limit, Class<? extends SimpleHandler> handler) {
        this.id = gameIdCount++;
        this.name = name;
        this.limit = limit;
        this.clients = new ArrayList<>();
        this.isOpen = true;
        this.handler = handler;
    }

    public int getLimit() {
        return limit;
    }

    public List<ClientEntity> getClients() {
        return clients;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String info(){
        return String.format("%s(%s/%s)", name, clients.size(), limit);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Optional<SimpleHandler> handle(ClientEntity client){
        try{
            return Optional.of(handler.getDeclaredConstructor(ClientEntity.class).newInstance(client));
        }catch(Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

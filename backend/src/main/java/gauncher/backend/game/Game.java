package gauncher.backend.game;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.handler.GameHandler;
import gauncher.backend.handler.MenuHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Game {
    private static int gameIdCount = 1;

    protected final int id;
    protected final String name;
    protected final int limit;
    protected final List<ClientEntity> clients;
    protected boolean isOpen;
    protected boolean isEnded;
    protected final Class<? extends GameHandler> handler;

    public Game(String name, int limit, Class<? extends GameHandler> handler) {
        this.id = gameIdCount++;
        this.name = name;
        this.limit = limit;
        this.clients = new ArrayList<>();
        this.isOpen = true;
        this.isEnded = false;
        this.handler = handler;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isFull(){
        return limit >= 0 && clients.size() >= limit;
    }

    public int getLimit() {
        return limit;
    }

    public List<ClientEntity> getClients() {
        return clients;
    }

    public String info() {
        return String.format("%s(%s/%s)", name, clients.size(), limit);
    }

    public void addClient(ClientEntity client) {
        if (!isFull()) {
            this.clients.add(client);
            if (isFull()) {
                MenuHandler.addGame(this.copy());
                isOpen = false;
            }
        }
    }

    public void removeClient(ClientEntity client) {
        this.clients.remove(client);
    }

    public Stream<ClientEntity> clientStream() {
        return this.clients.stream();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public Optional<? extends GameHandler> handle(ClientEntity client) {
        try {
            return Optional.of(
                    handler.getDeclaredConstructor(ClientEntity.class, Game.class)
                            .newInstance(client, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Game copy() {
        return new Game(name, limit, handler);
    }

    public void endGame(ClientEntity winner){
        String message;
        try{
            message = String.format("END %s won", winner.getUsername());
        }catch (NullPointerException e){
            message = "END nobody won";
        }
        broadcast(message);
        isEnded = true;
        clientStream()
                .filter(Objects::nonNull)
                .forEach(client -> new MenuHandler(client).start());
    }


    public void broadcast(String message) {
        this.clientStream()
                .map(ClientEntity::getPrinter)
                .forEach(printWriter -> printWriter.println(message));
    }
}

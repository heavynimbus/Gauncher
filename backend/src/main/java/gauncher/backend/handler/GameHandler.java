package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.game.Game;

public abstract class GameHandler extends SimpleHandler {
    protected Game game;

    public GameHandler(ClientEntity clientEntity, Game game) {
        super(clientEntity);
        this.game = game;
    }
}

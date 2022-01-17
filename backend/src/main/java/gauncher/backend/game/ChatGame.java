package gauncher.backend.game;

import gauncher.backend.handler.ChatHandler;

public class ChatGame extends Game {
    public ChatGame() {
        super("chat", -1, ChatHandler.class);
    }
}

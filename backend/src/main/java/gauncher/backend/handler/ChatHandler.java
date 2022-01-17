package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.Game;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class ChatHandler extends GameHandler {
    private final static Logger log = new Logger("ChatHandler");
    private boolean isInChat;

    public ChatHandler(ClientEntity clientEntity, Game game) {
        super(clientEntity, game);
        this.game.addClient(clientEntity);
    }

    @Override
    public void run() {
        isInChat = true;
        this.broadcast(String.format("%s joined the chat", this.clientEntity.getUsername()));
        while (isInChat) {
            try {
                String line = clientEntity.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(clientEntity);
            } catch (DisconnectException e) {
                log.error("%s has been disconnected", clientEntity);
                e.printStackTrace();
                this.leaveChat();
                try (ConnectionRepository connectionRepository = new ConnectionRepository()) {
                    connectionRepository.disconnect(clientEntity);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }

    private void handleLine(String line) {
        log.info("Received from %s: %s", clientEntity, line);
        if (line.startsWith("/")) handleCommand(line);
        else broadcast(String.format("%s: %s", clientEntity.getUsername(), line));
    }

    private void handleCommand(String line) {
        try {
            if (line.substring(0, 5).equalsIgnoreCase("/help")) {
                clientEntity.println("/help --> show this help message");
                clientEntity.println("/list --> get the list of people connected to the chat");
                clientEntity.println("/quit --> leave the chat");
            } else if (line.substring(0, 5).equalsIgnoreCase("/list")) {
                var list = game.clientStream()
                        .map(ClientEntity::getUsername)
                        .collect(Collectors.joining(","));
                clientEntity.println(String.format("SERVER: %s", list));
            } else if (line.substring(0, 5).equalsIgnoreCase("/quit")) {
                this.leaveChat();
                new MenuHandler(clientEntity).start();
            } else {
                throw new StringIndexOutOfBoundsException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            clientEntity.println("SERVER: invalid command, try /help");
        }
    }

    private void broadcast(String line) {
        this.game.broadcast(line);
    }

    private void leaveChat() {
        this.game.removeClient(this.clientEntity);
        this.broadcast(String.format("%s left the chat", this.clientEntity.getUsername()));
        isInChat = false;
    }
}

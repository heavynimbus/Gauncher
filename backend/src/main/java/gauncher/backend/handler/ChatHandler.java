package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatHandler extends SimpleHandler {
    private final static Logger log = new Logger("ChatHandler");
    private final static List<ClientEntity> CLIENT_ENTITIES = new ArrayList<>();

    public ChatHandler(ClientEntity clientEntity) {
        super(clientEntity);
        CLIENT_ENTITIES.add(clientEntity);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = clientEntity.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(clientEntity);
            } catch (DisconnectException e) {
                log.error("%s has been disconnected", clientEntity);
                e.printStackTrace();
                CLIENT_ENTITIES.remove(clientEntity);
                try {
                    new ConnectionRepository().disconnect(clientEntity);
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
                var list = CLIENT_ENTITIES.stream().map(ClientEntity::getUsername).collect(Collectors.joining(","));
                clientEntity.println(String.format("SERVER: %s", list));
            } else if (line.substring(0, 5).equalsIgnoreCase("/quit")) {
                CLIENT_ENTITIES.remove(clientEntity);
            } else {
                throw new StringIndexOutOfBoundsException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            clientEntity.println("SERVER: invalid command, try /help");
        }
    }

    private void broadcast(String line) {
        CLIENT_ENTITIES.stream()
                .map(ClientEntity::getPrinter)
                .forEach(printWriter -> printWriter.println(line));
    }
}

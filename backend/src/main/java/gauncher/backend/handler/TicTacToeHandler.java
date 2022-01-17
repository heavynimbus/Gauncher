package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.Game;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;

public class TicTacToeHandler extends GameHandler {

    private final static Logger log = new Logger("TicTacToeHandler");

    public TicTacToeHandler(ClientEntity clientEntity, Game game) {
        super(clientEntity, game);
        this.game.addClient(clientEntity);

    }

    @Override
    public void run() {
        while (!game.isEnded()) {
            try {
                String line = clientEntity.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(clientEntity);
            } catch (DisconnectException e) {
                log.error("%s has been disconnected", clientEntity);
                try (ConnectionRepository connectionRepository = new ConnectionRepository()) {
                    connectionRepository.disconnect(clientEntity);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (game.isOpen()) {
                    game.removeClient(clientEntity);
                } else {
                    game.clientStream().filter(client -> !client.equals(clientEntity)).findAny().ifPresent(client -> {
                        client.println("%s has been disconnected", clientEntity.getUsername());
                        game.endGame(client);
                    });
                }

                break;
            }
        }
    }

    private void handleLine(String line) {
        log.debug("Received from %s: %s", clientEntity, line);
        if (game.isOpen()) {
            clientEntity.println("KO Still waiting players");
            log.error("Still waiting players, %s/%s", game.getClients().size(), game.getLimit());
        } else {
            game.broadcast(line);
        }
    }
}

package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.Game;
import gauncher.backend.game.tictactoe.TicTacToeGame;
import gauncher.backend.game.tictactoe.TicTacToeType;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.Optional;

public class TicTacToeHandler extends GameHandler {

    private final static Logger log = new Logger("TicTacToeHandler");
    private TicTacToeType playerType;
    private TicTacToeGame tttGame;

    public TicTacToeHandler(ClientEntity clientEntity, Game game) {
        super(clientEntity, game);
        this.game.addClient(clientEntity);
        this.tttGame = (TicTacToeGame) game;
        tttGame.addPlayer(clientEntity);
    }

    private Optional<ClientEntity> getOther() {
        var otherPlayerType = tttGame.getPlayers().keySet().stream()
                .filter(key -> tttGame.getPlayers().get(key).equals(clientEntity))
                .findAny().orElse(TicTacToeType.NONE);
        if (otherPlayerType == TicTacToeType.NONE) {
            return Optional.empty();
        }
        return Optional.of(tttGame.getPlayers().get(otherPlayerType));
    }

    @Override
    public void run() {
        if (game.isFull()) {
            tttGame.getPlayers().keySet().forEach(type ->
                    tttGame.getPlayers().get(type).println("READY %s", type)
            );
            var optionalTicTacToeType = tttGame.getPlayers().keySet().stream()
                    .filter(key -> tttGame.getPlayers().get(key).equals(clientEntity))
                    .findAny();
            this.playerType = optionalTicTacToeType.orElseThrow();
            this.clientEntity.println("%s PLAY %s", playerType, tttGame);
        }
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

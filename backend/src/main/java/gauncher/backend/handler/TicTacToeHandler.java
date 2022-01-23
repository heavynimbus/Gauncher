package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.Game;
import gauncher.backend.game.tictactoe.TicTacToeGame;
import gauncher.backend.game.tictactoe.TicTacToeType;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;

public class TicTacToeHandler extends GameHandler {

    private final static Logger log = new Logger("TicTacToeHandler");
    private TicTacToeType playerType;
    private final TicTacToeGame tttGame;


    public TicTacToeHandler(ClientEntity clientEntity, Game game) {
        super(clientEntity, game);
        this.game.addClient(clientEntity);
        System.out.println("game.getClass() = " + game.getClass());
        this.tttGame = (TicTacToeGame) game;
        tttGame.addPlayer(clientEntity);
        tttGame.addHandler(this);
    }

    private void initPlayerType() {
        playerType = tttGame.getPlayers()
                .keySet().stream()
                .filter(key -> tttGame.getPlayers().get(key).equals(clientEntity))
                .findAny()
                .orElse(TicTacToeType.CROSS);

    }

    @Override
    public void run() {
        initPlayerType();
        if (game.isFull()) {
            tttGame.getPlayers().keySet().forEach(type -> {
                tttGame.getPlayers().get(type).println("READY %s", type);
            });
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
            clientEntity.println("KO Still waiting players %s/%s", game.getClients().size(), game.getLimit());
            log.error("Still waiting players, %s/%s", game.getClients().size(), game.getLimit());
        } else {
            if (tttGame.getCurrentPlayerType().equals(playerType)) {
                log.info(String.format("%s play tictactoe", clientEntity));
                var res = tttGame.play(line);
                if (res.startsWith("OK ")) {
                    log.info("Send to %s: %s", tttGame.getPlayers().get(tttGame.getCurrentPlayerType()), String.format("%s PLAY %s", tttGame.getCurrentPlayerType(), tttGame));
                    tttGame.getPlayers().get(tttGame.getCurrentPlayerType()).println(String.format("%s PLAY %s", tttGame.getCurrentPlayerType(), tttGame));
                } else if (res.startsWith("END")) {
                    //tttGame.broadcast(res);
                    //new MenuHandler(clientEntity).start();
                    //this.interrupt();
                    return;
                }
                clientEntity.println(res);
            } else {
                clientEntity.println(String.format("KO %s is playing", tttGame.getCurrentPlayerType()));
                log.error(String.format("%s want to play but it's the turn of the %s", playerType, tttGame.getCurrentPlayerType()));
            }
        }
    }
}

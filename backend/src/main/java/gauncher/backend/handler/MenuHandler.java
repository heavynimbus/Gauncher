package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ClientRepository;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.ChatGame;
import gauncher.backend.game.Game;
import gauncher.backend.game.tictactoe.TicTacToeGame;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MenuHandler extends SimpleHandler {
    private static final Logger log = new Logger("MenuHandler");
    private final ConnectionRepository connectionRepository;
    private static final List<Game> gameList = initGameList();
    private final AtomicBoolean isInMenu;

    public MenuHandler(ClientEntity clientEntity) {
        super(clientEntity);
        this.isInMenu = new AtomicBoolean(true);
        this.connectionRepository = new ConnectionRepository();
    }

    private static List<Game> initGameList() {
        var res = new ArrayList<Game>();
        res.add(new ChatGame());
        res.add(new TicTacToeGame());
        return res;
    }

    public static void addGame(Game game) {
        log.info("Creating a new %s room", game.getName());
        gameList.add(game);
    }

    @Override
    public void run() {
        while (isInMenu.get()) {
            try {
                String line = clientEntity.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(clientEntity);
            } catch (DisconnectException e) {
                log.error("The client %s has been disconnected", clientEntity);
                try (ConnectionRepository connectionRepository = new ConnectionRepository()) {
                    connectionRepository.disconnect(clientEntity);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                break;
            }
        }
        log.info("%s left the menu", clientEntity);
    }

    private void handleLine(String line) {
        try {
            log.info("Received from %s: %s", clientEntity, line);
            if (line.substring(0, 4).equalsIgnoreCase("list")) {
                handleList();
            } else if (line.substring(0, 4).equalsIgnoreCase("play")) {
                handlePlay(line);
            } else {
                throw new StringIndexOutOfBoundsException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            clientEntity.println("KO Wrong command, valid command are LIST, PLAY");
        }
    }

    private void handleList() {
        var infos = gameList.stream()
                .filter(Game::isOpen)
                .map(Game::info)
                .collect(Collectors.joining(","));
        clientEntity.println("OK %s %s", connectionRepository.getActiveCount(), infos);
    }

    private void handlePlay(String line) {
        String[] split = line.split(" ");
        var count = Arrays.stream(split).filter(elt -> !elt.isEmpty()).count();
        if (count != 2) {
            log.error("Bad request from %s: %s", clientEntity, line);
            clientEntity.println("KO invalid command, 'PLAY {gameName}'");
        } else {
            String gameName = split[1];
            gameList.stream()
                    .filter(Game::isOpen)
                    .filter(game -> game.getName().equals(gameName))
                    .findAny()
                    .ifPresentOrElse(game -> {
                        game.handle(clientEntity).ifPresentOrElse(handler -> {
                            clientEntity.println("OK enter in %s", game.getName());
                            isInMenu.set(false);
                            handler.start();
                        }, () -> {
                            clientEntity.println("KO Internal server error");
                            log.error("Internal server error on create handler");
                            isInMenu.set(true);
                        });
                    }, () -> {
                        clientEntity.println("KO game not found");
                        log.error("Game %s not found", gameName);
                    });
        }
    }
}

package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.game.Game;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenuHandler extends SimpleHandler {
    private static final Logger log = new Logger("MenuHandler");
    private static final List<Game> gameList = initGameList();

    public MenuHandler(ClientEntity clientEntity) {
        super(clientEntity);
    }

    private static List<Game> initGameList() {
        var res = new ArrayList<Game>();
        res.add(new Game("Chat", -1, ChatHandler.class));
        return res;
    }

    @Override
    public void run() {
        while (true) {
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
    }

    public void handleLine(String line) {
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

    public void handleList() {
        var infos = gameList.stream().map(Game::info).collect(Collectors.joining(","));
        clientEntity.println("OK %s", infos);
    }

    public void handlePlay(String line) {
        String[] split = line.split(" ");
        var count = Arrays.stream(split).filter(elt -> !elt.isEmpty()).count();
        if (count != 2) {
            log.error("Bad request from %s: %s", clientEntity, line);
            clientEntity.println("KO invalid command, 'PLAY {gameName}'");
        } else {
            String gameName = split[1];
            gameList.stream()
                    .filter(Game::isOpen).map(Game::info).forEach(log::debug);
            gameList.stream()
                    .filter(Game::isOpen)
                    .filter(game -> game.getName().equals(gameName))
                    .findAny()
                    .ifPresentOrElse(game -> {
                        log.debug("Enter in game %s", game);
                        clientEntity.println("enter in game %s", game.getName());
                        game.handle(clientEntity).ifPresentOrElse(Thread::start, ()->{
                            clientEntity.println("KO Internal server error");
                            log.error("Internal server error on create handler");
                        });
                    }, () -> {
                        clientEntity.println("KO game not found");
                        log.error("Game %s not found", gameName);
                    });
        }
    }
}

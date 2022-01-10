package gauncher.backend.v2.handler;

import static gauncher.backend.v2.util.StringUtil.formatForChat;

import gauncher.backend.v2.exception.DisconnectException;
import gauncher.backend.v2.logging.Logger;
import gauncher.backend.v2.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends SimpleHandler {
    private static final List<Player> players = new ArrayList<>();
    private final Logger log = new Logger("ChatHandler");

    public ChatHandler(Player player) {
        super(player);
        players.add(player);
    }

    private void sendAll(String message) {
        players.stream().map(Player::getPrinter)
                .forEach(printWriter -> printWriter.println(formatForChat(player, message)));
    }

    @Override
    public void run() {
        super.run();
        while (player.isLogged())
            try {
                var optionalLine = player.readLine();
                optionalLine.ifPresent(this::handleLine);
            } catch (DisconnectException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        log.info("End of ChatHandler for %s", player);
    }

    private void handleLine(String line) {
        log.info("Received from %s: %s", player, line);
        if (line.startsWith("/")) handleCommand(line);
        else sendAll(line);
    }

    private void handleCommand(String line){

    }


}

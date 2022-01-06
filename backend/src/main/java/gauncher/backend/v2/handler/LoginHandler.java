package gauncher.backend.v2.handler;

import static java.lang.String.format;

import gauncher.backend.v2.database.entity.Client;
import gauncher.backend.v2.logging.Logger;
import gauncher.backend.v2.player.Player;
import gauncher.backend.v2.database.repository.UserRepository;

import java.io.IOException;
import java.sql.SQLException;

public class LoginHandler extends SimpleHandler {
    private final static Logger log = new Logger("LoginHandler");
    private final UserRepository userRepository;

    public LoginHandler(Player player, Integer channelId) {
        super(player, channelId);
        this.userRepository = new UserRepository();
    }


    @Override
    public void run() {
        log.info(format("Starting login for %s", player));
        while (!player.isLogged()) {
            try {
                var optionalLine = player.readLine();
                optionalLine.ifPresent(this::handleLine);
            } catch (IOException e) {
                log.error("The client is disconnected.");
                e.printStackTrace();
                return;
            }
        }
    }

    private void handleLine(String s) {
        if (s.startsWith("LOGIN")) {
            handleLogin(s);
        } else if (s.startsWith("SIGN")) {
            handleSign(s);
        } else {
            log.error(format("Invalid command, %s have to login or sign in", player.getSocket().getRemoteSocketAddress()));
            this.player.println("KO Invalid command, you have to login");
        }
    }

    private void handleSign(String s) {
/*        try {
            Client client = new Client()
        } catch (SQLException e) {

        }*/
    }

    private void handleLogin(String s) {
        try {
            userRepository.getByUsername(s.split(" ")[1]).ifPresent(user -> {
                if (user.getPassword().equals(s.split(" ")[2])) {
                    player.setLogged(true);
                    player.println("OK Successfully logged");
                    log.info(String.format("%s successfully logged", player));
                } else player.println("KO Wrong username or password");
            });
        } catch (SQLException e) {
            e.printStackTrace();
            player.println("KO Internal Server Error");
        } catch (ArrayIndexOutOfBoundsException e) {
            player.println("KO invalid command: `LOGIN {username} {password}`");
        }
    }
}

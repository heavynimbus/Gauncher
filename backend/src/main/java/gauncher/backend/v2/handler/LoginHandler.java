package gauncher.backend.v2.handler;

import static java.lang.String.format;

import gauncher.backend.v2.database.entity.Client;
import gauncher.backend.v2.exception.DisconnectException;
import gauncher.backend.v2.logging.Logger;
import gauncher.backend.v2.player.Player;
import gauncher.backend.v2.database.repository.UserRepository;

import java.sql.SQLException;

public class LoginHandler extends SimpleHandler {
    private final static Logger log = new Logger("LoginHandler");
    private final UserRepository userRepository;

    public LoginHandler(Player player) {
        super(player);
        this.userRepository = new UserRepository();
    }


    @Override
    public void run() {
        log.info("Starting login for %s", player);
        while (!player.isLogged()) {
            try {
                var optionalLine = player.readLine();
                optionalLine.ifPresent(this::handleLine);
            } catch (DisconnectException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        log.info("End of LoginHandler for %s", player);
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
        try {
            var username = s.split(" ")[1];
            var password = s.split(" ")[2];
            var client = new Client(username, password);
            userRepository.persist(client).ifPresent(client1 -> {
                player.println("OK User successfully created");
                log.info(String.format("New user created: %s", client1.getUsername()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
            player.println("KO Internal server error");
        } catch (ArrayIndexOutOfBoundsException e) {
            player.println("KO Invalid command: `SIGN {username} {password}`");
        }
    }

    private void handleLogin(String s) {
        try {
            var username = s.split(" ")[1];
            var password = s.split(" ")[2];
            userRepository.getByUsername(username)
                    .filter(user -> user.getPassword().equals(password))
                    .ifPresentOrElse(user -> {
                        player.setLogged(true);
                        player.println("OK Successfully logged");
                        log.info(String.format("%s successfully logged", player));
                        new ChatHandler(player).start();
                    }, () -> {
                        log.error(String.format("%s used a wrong username or password", player));
                        player.println("KO wrong username or password");
                    });
        } catch (SQLException e) {
            log.error("An SQL exception has occurred");
            player.println("KO Internal Server Error");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("");
            player.println("KO invalid command: `LOGIN {username} {password}`");
        }
    }
}

package gauncher.backend.handler;

import gauncher.backend.database.entity.Client;
import gauncher.backend.database.repository.ClientRepository;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.Arrays;

public class LoginHandler extends SimpleHandler {
    private final static Logger log = new Logger("LoginHandler");

    public LoginHandler(Client client) {
        super(client);
    }

    @Override
    public void run() {
        log.info("Start handle login for %s", client);
        while (client.getId() == null) {
            try {
                String line = client.readLine();
                if (line != null) handleLine(line);
            } catch (DisconnectException e) {
                e.printStackTrace();
            }
        }
        log.info("%s is connected", client);
    }

    private boolean checkCommand(String command, String line) {
        try {
            return line.substring(0, command.length()).equalsIgnoreCase(command);
        } catch (StringIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    public void handleLine(String line) {
        if (checkCommand("LOGIN ", line)) {
            handleLogin(line);
        } else if (checkCommand("SIGN ", line)) {
            log.debug("Sign not yet implemented");
            client.println("Sign not yet implemented");
        } else {
            client.println("Wrong command, valid commands are LOGIN, SIGN");
        }
    }

    public void handleLogin(String line) {
        var splittedLine = line.split(" ");
        var stream = Arrays.stream(splittedLine);
        var count = stream.filter(elt -> !elt.isEmpty()).count();
        if (count == 3) {
            var username = splittedLine[1];
            var password = splittedLine[2];
            log.info(" %s try to login with username %s", this.client, username);
            try (ClientRepository repository = new ClientRepository()) {
                var optionalClient = repository.findByUsername(username);
                if (optionalClient.isPresent() && optionalClient.get().checkPassword(password)) {
                    var socket = client.getSocket();
                    this.client = optionalClient.get();
                    this.client.setSocket(socket);
                    client.println("OK Successfully logged in");
                    log.info("%s successfully logged in", client);
                } else {
                    log.error("Wrong username or password from %s", client);
                    this.client.println("KO Wrong username or password");
                }
            } catch (SQLException e) {
                log.error("Internal server error (check the stacktrace)");
                e.printStackTrace();
            }
        } else {
            log.error("Invalid command from %s", client);
        }

    }
}

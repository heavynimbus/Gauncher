package gauncher.backend.handler;

import gauncher.backend.database.entity.Client;
import gauncher.backend.database.repository.ClientRepository;
import gauncher.backend.exception.AlreadyExistsException;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;

import java.util.Arrays;

public class LoginHandler extends SimpleHandler {
    private final static Logger log = new Logger("LoginHandler");
    private final ClientRepository repository;

    public LoginHandler(Client client) {
        super(client);
        this.repository = new ClientRepository();
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
        new ChatHandler(client).start();
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
            handleSign(line);
        } else {
            client.println("Wrong command, valid commands are LOGIN, SIGN");
        }
    }

    public void handleSign(String line) {
        var splittedLine = line.split(" ");
        var count = Arrays.stream(splittedLine).filter(elt -> !elt.isEmpty()).count();
        if (count == 3) {
            var username = splittedLine[1];
            var password = splittedLine[2];
            try {
                log.info("Create account for %s with username %s", client, username);
                var createdClient = repository.create(new Client(username, password, true));
                /*var socket = client.getSocket();
                this.client = createdClient;
                this.client.setSocket(socket);*/
                client.println(String.format("OK %s created", createdClient.getUsername()));
            } catch (AlreadyExistsException e) {
                log.error("%s can't sign with username %s, already used", client, username);
                client.println("KO username already used");
            }
        } else {
            log.error("Invalid command from %s", client);
        }
    }

    public void handleLogin(String line) {
        var splittedLine = line.split(" ");
        var count = Arrays.stream(splittedLine).filter(elt -> !elt.isEmpty()).count();
        if (count == 3) {
            var username = splittedLine[1];
            var password = splittedLine[2];
            log.info(" %s try to login with username %s", this.client, username);
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
        } else {
            log.error("Invalid command from %s", client);
        }

    }
}

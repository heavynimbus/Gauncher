package gauncher.backend.handler;

import gauncher.backend.database.entity.ClientEntity;
import gauncher.backend.database.repository.ClientRepository;
import gauncher.backend.database.repository.ConnectionRepository;
import gauncher.backend.exception.AlreadyExistsException;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;

import java.sql.SQLException;
import java.util.Arrays;

public class LoginHandler extends SimpleHandler {
    private final static Logger log = new Logger("LoginHandler");
    private final ClientRepository repository;

    public LoginHandler(ClientEntity clientEntity) {
        super(clientEntity);
        this.repository = new ClientRepository();
    }

    @Override
    public void run() {
        log.info("Start handle login for %s", clientEntity);
        while (clientEntity.getId() == null) {
            try {
                String line = clientEntity.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(clientEntity);
            } catch (DisconnectException e) {
                e.printStackTrace();
                break;
            }
        }
        if (clientEntity.getId() != null) {
            try {
                var repo = new ConnectionRepository();
                repo.create(clientEntity);
                repo.close();
                repository.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //new ChatHandler(clientEntity).start();
            new MenuHandler(clientEntity).start();
        } else log.error("An error has occurred, please take a look at the stacktrace");
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
            clientEntity.println("KO Wrong command, valid commands are LOGIN, SIGN");
        }
    }

    public void handleSign(String line) {
        var splitLine = line.split(" ");
        var count = Arrays.stream(splitLine).filter(elt -> !elt.isEmpty()).count();
        if (count == 3) {
            var username = splitLine[1];
            var password = splitLine[2];
            try {
                log.info("Create account for %s with username %s", clientEntity, username);
                var createdClient = repository.create(new ClientEntity(username, password, true));
                clientEntity.println(String.format("OK %s created", createdClient.getUsername()));
            } catch (AlreadyExistsException e) {
                log.error("%s can't sign with username %s, already used", clientEntity, username);
                clientEntity.println("KO username already used");
            }
        } else {
            log.error("Invalid command from %s", clientEntity);
            clientEntity.println("KO invalid command, 'LOGIN user password'");
        }
    }

    public void handleLogin(String line) {
        var splitLine = line.split(" ");
        var count = Arrays.stream(splitLine).filter(elt -> !elt.isEmpty()).count();
        if (count == 3) {
            var username = splitLine[1];
            var password = splitLine[2];
            log.info(" %s try to login with username %s", this.clientEntity, username);
            var optionalClient = repository.findByUsername(username);
            if (optionalClient.isPresent() && optionalClient.get().checkPassword(password)) {
                var socket = clientEntity.getSocket();
                this.clientEntity = optionalClient.get();
                this.clientEntity.setSocket(socket);
                clientEntity.println("OK Successfully logged in");
                log.info("%s successfully logged in", clientEntity);
            } else {
                log.error("Wrong username or password from %s", clientEntity);
                this.clientEntity.println("KO Wrong username or password");
            }
        } else {
            log.error("Invalid command from %s", clientEntity);
        }

    }
}

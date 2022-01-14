package gauncher.backend.handler;

import gauncher.backend.database.entity.Client;
import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatHandler extends SimpleHandler {
    private final static Logger log = new Logger("ChatHandler");
    private final static List<Client> clients = new ArrayList<>();

    public ChatHandler(Client client) {
        super(client);
        clients.add(client);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = client.readLine();
                if (line != null) handleLine(line);
                else throw new DisconnectException(client);
            } catch (DisconnectException e) {
                log.error("%s has been disconnected", client);
                e.printStackTrace();
                clients.remove(client);
                break;
            }
        }
    }

    private void handleLine(String line) {
        log.info("Received from %s: %s", client, line);
        if (line.startsWith("/")) handleCommand(line);
        else broadcast(String.format("%s: %s", client.getUsername(), line));
    }

    private void handleCommand(String line) {
        try {
            if (line.substring(0, 5).equalsIgnoreCase("/help")) {
                client.println("/help --> show this help message");
                client.println("/list --> get the list of people connected to the chat");
                client.println("/quit --> leave the chat");
            } else if (line.substring(0, 5).equalsIgnoreCase("/list")) {
                var list = clients.stream().map(Client::getUsername).collect(Collectors.joining(","));
                client.println(String.format("SERVER: %s", list));
            } else if (line.substring(0, 5).equalsIgnoreCase("/quit")) {
                clients.remove(client);
            } else {
                throw new StringIndexOutOfBoundsException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            client.println("SERVER: invalid command, try /help");
        }
    }

    private void broadcast(String line) {
        clients.stream()
                .map(Client::getPrinter)
                .forEach(printWriter -> printWriter.println(line));
    }
}

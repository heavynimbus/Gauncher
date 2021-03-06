package gauncher.backend.database.entity;


import gauncher.backend.exception.DisconnectException;
import gauncher.backend.logging.Logger;
import gauncher.backend.service.PasswordService;
import gauncher.backend.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;


public class ClientEntity extends Entity {
    public static final AtomicLong NB_CLIENTS = new AtomicLong(0);
    private static final Logger log = new Logger("Client");
    private static final PasswordService passwordService = new PasswordService();
    private static final StringUtil stringUtil = new StringUtil();
    private String username;
    private String password;

    private Socket socket;
    private PrintWriter printer;
    private BufferedReader reader;

    public ClientEntity(Socket socket) {
        super();
        setSocket(socket);
    }

    public ClientEntity(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public ClientEntity(String username, String password, boolean hashPassword) {
        this.username = username;
        if (hashPassword) this.setPassword(password);
        else this.password = password;
    }

    public ClientEntity(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.username = resultSet.getString("username");
        this.password = resultSet.getString("password");
    }

    public String getUsername() {
        return username;
    }

    public void setSocket(Socket socket) {
        try {
            this.socket = socket;
            this.printer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getPrinter() {
        return printer;
    }

    public void setPassword(String password) {
        this.password = passwordService.hash(password);
    }

    public boolean checkPassword(String password) {
        return passwordService.hash(password).equals(this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void println(String format, Object... args) {
        printer.println(stringUtil.format(format, args));
    }

    public String readLine() throws DisconnectException {
        try {
            return this.reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DisconnectException(this);
        }
    }

    @Override
    public String getInsertColumnNames() {
        return "(username, password)";
    }

    @Override
    public String getInsertValueString() {
        return String.format("('%s','%s')", username, password);
    }

    @Override
    public String getUpdateValueString() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Client(id=%s, username=%s, socket=%s)",
                id, username, (socket != null) ? socket.getRemoteSocketAddress() : "null");
    }

    @Override
    public String getTableName() {
        return "client";
    }

}



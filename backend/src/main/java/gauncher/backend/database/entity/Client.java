package gauncher.backend.database.entity;


import gauncher.backend.exception.DisconnectException;
import gauncher.backend.service.PasswordService;
import gauncher.backend.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;


public class Client extends Entity {
    private static final PasswordService passwordService = new PasswordService();
    private static final StringUtil stringUtil = new StringUtil();
    private String username;
    private String password;
    private Socket socket;
    private PrintWriter printer;
    private BufferedReader reader;

    public Client(Socket socket) {
        super();
        try {
            this.socket = socket;
            this.printer = new PrintWriter(socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
        public Client(ResultSet resultSet) throws SQLException {
            super(resultSet);
            this.username = resultSet.getString("username");
            this.password = resultSet.getString("password");
        }
    */
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
    public String getInsertValueString() {
        return String.format("('%s','%s')", username, password);
    }

    @Override
    public String getUpdateValueString() {
        return null;
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", socket=" + socket +
                ", printer=" + printer +
                ", reader=" + reader +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}



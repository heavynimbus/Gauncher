package gauncher.frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.beans.property.SimpleStringProperty;

public class Client {
  private static final int SERVER_IP = 8080;
  private Socket socket;
  private PrintWriter printWriter;
  private BufferedReader reader;
  private SimpleStringProperty pseudo;

  public Client() {}

  public Client bindSocket(Socket socket) throws IOException {
    this.socket = socket;
    this.printWriter = new PrintWriter(socket.getOutputStream(), true);
    this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.pseudo = new SimpleStringProperty();
    return this;
  }

  public void setPseudo(String pseudo) {
    this.pseudo.set(pseudo);
  }

  public SimpleStringProperty getPseudo() {
    return this.pseudo;
  }

  public String readLine() throws IOException {

    return this.reader.readLine();
  }

  public void println(Object object) {
    this.printWriter.println(object);
  }
}

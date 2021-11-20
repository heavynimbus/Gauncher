package gauncher.frontend2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private static final int SERVER_IP = 8080;
  private Socket socket;
  private PrintWriter printWriter;
  private BufferedReader reader;

  public Client(String hostname) throws IOException {
    this.setSocket(new Socket(hostname, SERVER_IP));
  }

  public void setSocket(Socket socket) throws IOException {
    this.socket = socket;
    this.printWriter = new PrintWriter(socket.getOutputStream());
    this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }
}

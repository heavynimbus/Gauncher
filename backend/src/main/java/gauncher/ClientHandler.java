package gauncher;

import java.net.Socket;

public class ClientHandler extends Thread {

  Socket client;

  public ClientHandler(Socket client){
    this.client = client;
  }

}

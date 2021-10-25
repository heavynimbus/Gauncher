package gauncher.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SimpleHandler extends Thread{
  protected Socket client;
  protected PrintWriter out;
  protected BufferedReader in;

  public SimpleHandler(Socket client) throws IOException {
    this.client = client;
    this.out = new PrintWriter(client.getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
  }
}

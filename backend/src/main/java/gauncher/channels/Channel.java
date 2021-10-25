package gauncher.channels;

import java.net.Socket;
import java.util.ArrayList;

public class Channel {
  private String name;
  private int limit;
  private ArrayList<Socket> clients;
}

package gauncher.backend.v1.channel.chat;

import gauncher.backend.v1.channel.Channel;
import gauncher.backend.v1.handlers.ChatHandler;
import gauncher.backend.v1.logging.Logger;

public class ChatChannel extends Channel {
  private final Logger log;

  public ChatChannel(){
    super("chat", -1, "A chat where everybody can talk", ChatHandler.class);
    this.log = new Logger("ChatChannel");
  }
}

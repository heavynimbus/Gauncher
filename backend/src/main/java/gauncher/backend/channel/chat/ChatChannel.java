package gauncher.backend.channel.chat;

import gauncher.backend.channel.Channel;
import gauncher.backend.handlers.ChatHandler;
import gauncher.backend.logging.Logger;

public class ChatChannel extends Channel {
  private final Logger log;

  public ChatChannel(){
    super("chat", -1, "A chat where everybody can talk", ChatHandler.class);
    this.log = new Logger("ChatChannel");
  }
}

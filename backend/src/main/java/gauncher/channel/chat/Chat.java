package gauncher.channel.chat;

import gauncher.channel.Channel;
import gauncher.handlers.ChatHandler;
import gauncher.logging.Logger;

public class Chat extends Channel {
  private final Logger log;

  public Chat(){
    super("chat", -1, "A chat where everybody can talk", ChatHandler.class);
    this.log = new Logger("ChatChannel");
  }
}

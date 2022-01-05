package gauncher.backend.v1.channel;

import gauncher.backend.v1.handlers.CommandHandler;

public class CommandChannel extends Channel {

  public CommandChannel() {
    super("CommandChannel", -1, "The channel for handling menu", CommandHandler.class);
  }
}

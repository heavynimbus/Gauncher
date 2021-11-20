package gauncher.backend.channel;

import gauncher.backend.handlers.CommandHandler;

public class CommandChannel extends Channel {

  public CommandChannel() {
    super("CommandChannel", -1, "The channel for handling menu", CommandHandler.class);
  }
}

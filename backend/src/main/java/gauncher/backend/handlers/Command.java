package gauncher.backend.handlers;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Command {
  HELP,
  LOGIN,
  LIST,
  ENTER,
  QUIT;

  public static Stream<Command> stream() {
    return Arrays.stream(Command.values());
  }
}

package gauncher.handlers;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Command {
  LIST,
  ENTER,
  QUIT;

  public static Stream<Command> stream() {
    return Arrays.stream(Command.values());
  }
}

package gauncher.channel;

import gauncher.logging.Logger;

public class Minesweeper extends Channel {

  private final Logger log;
  private boolean[][] board;

  public Minesweeper(String description) {
    super("Minesweeper", 4, description);
    this.log = new Logger("");
    initBoard(20, 20);
  }

  private void initBoard(int nbLines, int nbColumns) {
    log.debug("init board");
    this.board = new boolean[nbLines][nbColumns];
  }
}

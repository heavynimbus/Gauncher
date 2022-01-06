package gauncher.backend;

import gauncher.backend.v1.ServerV1;
import gauncher.backend.v2.ServerV2;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.spec.ECField;

public class Server {
  private static final int SERVER_VERSION = 2;

  public static void main(String[] args) throws Exception {
    switch (SERVER_VERSION) {
      case 1:
        ServerV1.main(args);
        break;
      case 2:
        ServerV2.main(args);
        break;
    }
  }
}

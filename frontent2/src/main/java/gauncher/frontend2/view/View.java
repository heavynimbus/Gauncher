package gauncher.frontend2.view;

import static java.lang.String.format;

import gauncher.frontend2.exception.UnprocessableViewException;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class View extends Scene {

  protected View(String FXML_FILE_PATH) throws UnprocessableViewException {
    super(
        loadFXML(FXML_FILE_PATH)
            .orElseThrow(
                () ->
                    new UnprocessableViewException(format("%s can't be loaded", FXML_FILE_PATH))));
  }

  protected static Optional<Parent> loadFXML(String filePath) {
    try {
      return Optional.of(new FXMLLoader().load(View.class.getResourceAsStream(filePath)));
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}

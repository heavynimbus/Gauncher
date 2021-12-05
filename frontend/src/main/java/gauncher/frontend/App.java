package gauncher.frontend;

import gauncher.frontend.client.Client;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.view.ConnectionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** JavaFX App */
public class App extends Application {
  public static Client client;
  public static final Logger log = new Logger("App");
  private static Stage currentStage;

  public static void setCurrentScene(Scene scene) {
    currentStage.setScene(scene);
  }
  public static boolean isShowing(){
    return currentStage.isShowing();
  }
  @Override
  public void start(Stage stage) {
    App.currentStage = stage;
    stage.setResizable(false);
    try {
      setCurrentScene(new ConnectionView());
      stage.show();
    } catch (UnprocessableViewException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }
}

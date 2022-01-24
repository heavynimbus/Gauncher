package gauncher.frontend;

import gauncher.frontend.client.Client;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.view.ConnectionView;
import gauncher.frontend.view.LauncherView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/** JavaFX App */
public class App extends Application {
  public static Client client;
  public static final Logger log = new Logger("App");
  private static Stage currentStage;

  public static void setCurrentScene(Scene scene) {
    currentStage.setScene(scene);
  }

  public static void setCurrentStage(Stage stage) {
    App.currentStage = stage;
  }

  public static Stage getCurrentStage() {
    return App.currentStage;
  }

  public static void setOnCloseRequest(EventHandler<WindowEvent> handler){
    currentStage.setOnCloseRequest(handler);
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
//      var s = new Stage();
//      s.setScene(new ConnectionView());
//      s.show();
    } catch (UnprocessableViewException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }
}

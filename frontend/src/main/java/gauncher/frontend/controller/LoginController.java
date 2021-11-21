package gauncher.frontend.controller;

import gauncher.frontend.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

  @FXML public Button continueButton;

  @FXML public TextField pseudo;
  public SimpleStringProperty pseudoValue;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      App.client.println("USERNAME");
      String username = App.client.readLine();
      pseudoValue = new SimpleStringProperty(username);
      pseudo.textProperty().bind(pseudoValue);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void goToChannelChoice() {
    // change current scene
  }
}

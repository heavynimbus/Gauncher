package gauncher.frontend.controller;

import static java.lang.String.format;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.util.TextParser;
import gauncher.frontend.view.ChatView;
import gauncher.frontend.view.ConnectionView;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements Initializable {

  private Logger log = new Logger("LoginController");

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
  public void inputValue(KeyEvent event) {
    TextParser.getDefaultInstance(pseudoValue, pseudo)
        .addAction(
            (e, stringProperty) -> {
              if (e.getCode().equals(KeyCode.ENTER)) continueButton.requestFocus();
            })
        .inputValue(event);
  }

  @FXML
  public void login() throws UnprocessableViewException {
    var pseudoValue = this.pseudoValue.get();
    if (pseudoValue == null || pseudoValue.isEmpty() || pseudoValue.isBlank()) {
      pseudo.setStyle("-fx-border-color: red;");
      pseudo.setOnKeyPressed(
          (e) -> {
            pseudo.setStyle("");
            this.inputValue(e);
            pseudo.setOnKeyPressed(this::inputValue);
          });
    }
    try {
      App.client.println(format("LOGIN %s", pseudoValue));
      var response = App.client.readLine();
      log.info(format("%s is not already used", pseudoValue));
      if (response.startsWith("OK")) {
        App.client.println("USERNAME");
        response = App.client.readLine();
        App.client.setPseudo(response);
        App.setCurrentScene(new ChatView());
      }
    } catch (SocketException e) {
      log.error("Client have been disconnected from the server");
      App.setCurrentScene(new ConnectionView());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

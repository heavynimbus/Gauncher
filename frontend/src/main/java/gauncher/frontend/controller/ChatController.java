package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gauncher.frontend.view.LauncherView;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ChatController implements Initializable {

  public Logger log = new Logger("ChatController");

  @FXML public TextArea messages;

  private SimpleStringProperty messageValue;

  @FXML public BorderPane borderPane;

  @FXML private Button buttonSend;

  @FXML private ListView<?> chatPane;

  @FXML public TextArea messageBox;

  public SimpleStringProperty messageBoxValue;

  @FXML private Label onlineCountLabel;
  private SimpleStringProperty onlineCountLabelValue;

  @FXML private HBox onlineUsersHbox;

  @FXML private ListView<?> userList;

  @FXML public Label usernameLabel;

  public SimpleStringProperty usernameLabelValue;

  @FXML
  void sendButtonAction() {
    App.client.println(messageBoxValue.get());
    messageBoxValue.set("");
  }

  private boolean setOnlineCountLabelValue(String receivedMessage) {
    try {
      if (receivedMessage.startsWith("Online Users:")) {
        onlineCountLabelValue.set(receivedMessage.replace("Online Users:", ""));
        System.out.println("=" + onlineCountLabelValue.get());
        return true;
      } else if (receivedMessage.endsWith(" joined the chat")
          && !receivedMessage.startsWith(App.client.getPseudo().get())) {
        onlineCountLabelValue.set(
            String.valueOf(Integer.parseInt(onlineCountLabelValue.get()) + 1));
        System.out.println("+1:" + onlineCountLabelValue.get());
        return true;
      } else if (receivedMessage.endsWith(" left the chat")) {
        onlineCountLabelValue.set(
            String.valueOf(Integer.parseInt(onlineCountLabelValue.get()) - 1));
        System.out.println("-1:" + onlineCountLabelValue.get());
        return true;
      }
    } catch (NumberFormatException e) {
      return false;
    }
    return false;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    App.client.println("Enter Chat");
    messageValue = new SimpleStringProperty("");
    messages.textProperty().bind(messageValue);
    messageBoxValue = new SimpleStringProperty("");
    messageBox.textProperty().bind(messageBoxValue);
    usernameLabel.textProperty().bind(App.client.getPseudo());
    onlineCountLabelValue = new SimpleStringProperty("");
    onlineCountLabel.textProperty().bind(onlineCountLabelValue);
    new Thread(
            () -> {
              try {
                while (App.isShowing()) {
                  String line = App.client.readLine();
                  if (!setOnlineCountLabelValue(line)) {
                    log.debug(line);
                    if (messageValue.get().length() > 0)
                      messageValue.set(messageValue.get() + "\n" + line);
                  } else messageValue.set(line);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  @FXML
  public void inputValue(KeyEvent keyEvent) {
    var value = messageBoxValue.get();
    var code = keyEvent.getCode();

    if (code.equals(KeyCode.BACK_SPACE)) {
      if (!messageBoxValue.get().isEmpty()) {
        messageBoxValue.set(messageBoxValue.get().substring(0, messageBoxValue.get().length() - 1));
        messageBox.positionCaret(messageBoxValue.get().length());
      }
    } else if (keyEvent.getText() != null) {
      messageBoxValue.set(value + keyEvent.getText());
      messageBox.positionCaret(messageBoxValue.get().length());
    }

    if (code.equals(KeyCode.ENTER) && keyEvent.getText() != null) {
      App.client.println(value);
      messageBoxValue.set("");
    }
  }

  @FXML
  void previousView(MouseEvent event) throws UnprocessableViewException {
      App.setCurrentScene(new LauncherView());
  }
}
// (code.isDigitKey() || code.isLetterKey() || code.isWhitespaceKey()) && value != null) {

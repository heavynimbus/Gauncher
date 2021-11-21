package gauncher.frontend2.controller;

import static java.lang.String.format;

import gauncher.frontend2.App;
import gauncher.frontend2.task.ConnectionTask;
import gauncher.frontend2.exception.UnprocessableViewException;
import gauncher.frontend2.logging.Logger;
import gauncher.frontend2.view.ChatView;
import gauncher.frontend2.view.LoginView;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ConnectionController implements Initializable {

  public static final Logger LOG = new Logger("ConnectionController");

  @FXML public TextField ipField1;
  private SimpleStringProperty ipFieldValue1;

  @FXML public TextField ipField2;
  private SimpleStringProperty ipFieldValue2;

  @FXML public TextField ipField3;
  private SimpleStringProperty ipFieldValue3;

  @FXML public TextField ipField4;
  private SimpleStringProperty ipFieldValue4;

  @FXML public Button submitButton;

  @FXML public Label errorLabel;
  private SimpleStringProperty errorLabelValue;

  @FXML public ProgressIndicator progressIndicator;
  private SimpleIntegerProperty progressIndicatorValue;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    errorLabelValue = new SimpleStringProperty();
    ipFieldValue1 = new SimpleStringProperty("127");
    ipFieldValue2 = new SimpleStringProperty("0");
    ipFieldValue3 = new SimpleStringProperty("0");
    ipFieldValue4 = new SimpleStringProperty("1");
    progressIndicatorValue = new SimpleIntegerProperty(0);
    errorLabel.textProperty().bind(errorLabelValue);
    ipField1.textProperty().bind(ipFieldValue1);
    ipField2.textProperty().bind(ipFieldValue2);
    ipField3.textProperty().bind(ipFieldValue3);
    ipField4.textProperty().bind(ipFieldValue4);
    progressIndicator.setVisible(false);
  }

  private String computeAddr() {
    return String.join(
        ".",
        List.of(
            ipFieldValue1.get(), ipFieldValue2.get(), ipFieldValue3.get(), ipFieldValue4.get()));
  }

  @FXML
  public void tryToConnect() {
    var ipAddr = this.computeAddr();
    if (ipAddr.isBlank()) {
      LOG.error("IP addr cannot be blank");
      showErrorLabel("IP address cannot be blank");
    }
    LOG.info(format("Try to connect to %s", ipAddr));
    submitButton.setDisable(true);
    progressIndicator.setVisible(true);
    ConnectionTask connectionTask = new ConnectionTask(ipAddr);
    connectionTask.addEventHandler(
        WorkerStateEvent.WORKER_STATE_FAILED,
        (event) -> {
          var errorMessage = format("Connection with %s failed", ipAddr);
          LOG.error(errorMessage);
          progressIndicator.setVisible(false);
          this.showErrorLabel(errorMessage);
        });

    connectionTask.addEventHandler(
        WorkerStateEvent.WORKER_STATE_SUCCEEDED,
        (event) -> {
          App.client = connectionTask.getValue();
          LOG.info(format("Connection with %s succeeded", ipAddr));
          progressIndicator.setVisible(false);
          try {
            var loginView = new LoginView();
            var chatView = new ChatView();
            App.setCurrentScene(chatView);
          } catch (UnprocessableViewException ignored) {
          }
        });

    progressIndicator.progressProperty().bind(connectionTask.progressProperty());
    new Thread(connectionTask).start();
  }

  private Optional<Object> getById(int id, Object... values) {
    switch (id) {
      case 1:
        return Optional.of(values[0]);
      case 2:
        return Optional.of(values[1]);
      case 3:
        return Optional.of(values[2]);
      case 4:
        return Optional.of(values[3]);
      default:
        return Optional.empty();
    }
  }

  private Optional<SimpleStringProperty> getIpFieldValueById(int id) {
    return getById(id, ipFieldValue1, ipFieldValue2, ipFieldValue3, ipFieldValue4)
        .map(value -> (SimpleStringProperty) value);
  }

  private Optional<TextField> getIpFieldById(int id) {
    return getById(id, ipField1, ipField2, ipField3, ipField4).map(value -> (TextField) value);
  }

  private void getFocusAndSetCursor(TextField textField) {
    textField.requestFocus();
    textField.positionCaret(textField.textProperty().get().length());
  }

  @FXML
  public void inputValues(KeyEvent event) {
    var source = (TextField) event.getSource();
    var currentId = Integer.parseInt(source.getId().replace("ipField", ""));
    var code = event.getCode();
    if (code.isDigitKey()) {
      var value = getIpFieldValueById(currentId).orElseThrow();
      if (value.get().length() > 3) value.set(event.getText());
      else {
        try {
          int toAdd = Integer.parseInt(event.getText());
          var length = value.get().length();
          System.out.println("length = " + length);
          if (length >= 3) value.set("");
          value.set(value.get() + toAdd);
        } catch (NumberFormatException ignored) {
        }
        if (value.get().length() >= 3)
          getIpFieldById(currentId + 1).ifPresent(this::getFocusAndSetCursor);
      }
    } else if (code.equals(KeyCode.BACK_SPACE)) {

      getIpFieldValueById(currentId)
          .ifPresent(
              value -> {
                if (value.get().isBlank())
                  getIpFieldById(currentId - 1)
                      .ifPresentOrElse(
                          this::getFocusAndSetCursor, () -> this.getFocusAndSetCursor(ipField1));
                else {
                  value.set(value.get().substring(0, value.get().length() - 1));
                }
              });
    } else if (code.equals(KeyCode.TAB)) {
      getIpFieldById(currentId + 1)
          .ifPresentOrElse(this::getFocusAndSetCursor, () -> this.getFocusAndSetCursor(ipField1));
    } else if (code.equals(KeyCode.ENTER)) {
      submitButton.requestFocus();
    }
    getIpFieldValueById(currentId)
        .ifPresent(
            value -> {
              var intValue = Integer.parseInt(value.get());
              if (intValue < 0 || intValue > 255) {

                source.setStyle(
                    source.getStyle() + ";-fx-border-color: red ; -fx-border-width: 2px ;");
              } else {
                source.setStyle(
                    source.getStyle() + ";-fx-border-color: green ; -fx-border-width: 2px ;");
              }
            });
  }

  private void showErrorLabel(String message) {
    this.errorLabel.setVisible(true);
    this.errorLabelValue.set(message);
  }
}

package gauncher.frontend.controller;

import static java.lang.String.format;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.task.ConnectionTask;
import gauncher.frontend.util.TextParser;
import gauncher.frontend.view.LoginView;
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
          submitButton.setDisable(false);
          this.showErrorLabel(errorMessage);
        });

    connectionTask.addEventHandler(
        WorkerStateEvent.WORKER_STATE_SUCCEEDED,
        (event) -> {
          App.client = connectionTask.getValue();
          LOG.info(format("Connection with %s succeeded", ipAddr));
          progressIndicator.setVisible(false);
          try {
            App.setCurrentScene(new LoginView());
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
    getIpFieldValueById(currentId)
        .ifPresent(
            value ->
                TextParser.getDefaultInstance(value, source)
                    .addFilter(
                        e -> { // Only accepts numbers
                          try {
                            Integer.parseInt(e.getText());
                            return true;
                          } catch (NumberFormatException ignored) {
                            return false;
                          }
                        })
                    .addAction( // request focus on last field
                        (e, stringProperty) -> {
                          if (e.getCode().equals(KeyCode.BACK_SPACE)
                              && stringProperty.get().length() == 0)
                            getIpFieldById(currentId - 1)
                                .ifPresentOrElse(TextField::requestFocus, ipField1::requestFocus);
                        })
                    .addEndAction( // request focus on next field
                        (input, stringProperty) -> {
                          if (stringProperty.get().length() >= 3)
                            getIpFieldById(currentId + 1)
                                .ifPresentOrElse(
                                    TextField::requestFocus, submitButton::requestFocus);
                        })
                    .inputValue(event));
  }

  private void showErrorLabel(String message) {
    this.errorLabel.setVisible(true);
    this.errorLabelValue.set(message);
  }
}

package gauncher.frontend2.controller;

import static java.lang.String.format;

import gauncher.frontend2.App;
import gauncher.frontend2.client.Client;
import gauncher.frontend2.logging.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.errorLabelValue = new SimpleStringProperty();
    this.ipFieldValue1 = new SimpleStringProperty("127");
    this.ipFieldValue2 = new SimpleStringProperty("0");
    this.ipFieldValue3 = new SimpleStringProperty("0");
    this.ipFieldValue4 = new SimpleStringProperty("1");
    errorLabel.textProperty().bind(errorLabelValue);
    ipField1.textProperty().bind(ipFieldValue1);
    ipField2.textProperty().bind(ipFieldValue2);
    ipField3.textProperty().bind(ipFieldValue3);
    ipField4.textProperty().bind(ipFieldValue4);
  }

  private String computeAddr() {
    return "localhost";
  }

  @FXML
  public void tryToConnect() {
    var ipAddr = this.computeAddr();
    if (ipAddr.isBlank()) {
      LOG.error("IP addr cannot be blank");
      showErrorLabel("IP address cannot be blank");
    }
    LOG.info(format("Try to connect to %s", ipAddr));
    try {
      App.client = new Client(ipAddr);
    } catch (IOException e) {
      showErrorLabel("Connection failed");
      LOG.error(format("Connection to %s failed", ipAddr));
    }
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

  @FXML
  public void inputValues(KeyEvent event) {
    System.out.println(event);
    var source = (TextField) event.getSource();
    var currentId = Integer.parseInt(source.getId().replace("ipField", ""));
    var code = event.getCode();
    System.out.println("code = " + code);
    if (code.isDigitKey()) {
      var value = getIpFieldValueById(currentId).orElseThrow();
      if (value.get().length() > 3) value.set(event.getText());
      else {
        value.set(value.get() + event.getText());
        if (value.get().length() > 3) getIpFieldById(currentId + 1).ifPresent(Node::requestFocus);
      }
    } else if (code.equals(KeyCode.BACK_SPACE)) {

    } else if (code.equals(KeyCode.TAB)) {
      LOG.info("currentId: " + currentId);
      getIpFieldById(currentId + 1).ifPresentOrElse(Node::requestFocus, ipField1::requestFocus);
    } else if (code.equals(KeyCode.ENTER)) {
      submitButton.requestFocus();
    }
  }

  private void showErrorLabel(String message) {
    this.errorLabelValue.set(message);
  }
}

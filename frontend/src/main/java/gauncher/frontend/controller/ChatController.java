package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    public Logger log;

    @FXML
    public TextArea messages;

    private SimpleStringProperty messageValue;

    @FXML
    public BorderPane borderPane;

    @FXML
    private Button buttonSend;

    @FXML
    private ListView<?> chatPane;

    @FXML
    public TextArea messageBox;

    public SimpleStringProperty messageBoxValue;

    @FXML
    private Label onlineCountLabel;

    @FXML
    private HBox onlineUsersHbox;

    @FXML
    private ListView<?> userList;

    @FXML
    public Label usernameLabel;

    public SimpleStringProperty usernameLabelValue;

    @FXML
    void sendButtonAction() {
        App.client.println(messageBoxValue.get());
        messageBoxValue.set("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.log = new Logger("ChatController");
            App.client.println("Enter Chat");
            String confirme = App.client.readLine();
            this.messageValue = new SimpleStringProperty(confirme);
            this.messages.textProperty().bind(messageValue);
            this.messageBoxValue = new SimpleStringProperty("");
            this.messageBox.textProperty().bind(messageBoxValue);
            usernameLabelValue = new SimpleStringProperty(App.client.getPseudo());
            usernameLabel.textProperty().bind(usernameLabelValue);
            new Thread(() -> {
                try {
                    while (true) {
                        String line = App.client.readLine();
                        messageValue.set(messageValue.get() + "\n" + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
//(code.isDigitKey() || code.isLetterKey() || code.isWhitespaceKey()) && value != null) {

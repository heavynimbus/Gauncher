package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import gauncher.frontend.view.LauncherView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ChatController implements Initializable {

    public Logger log = new Logger("ChatController");

    @FXML
    public TextArea messages;

    @FXML
    public BorderPane borderPane;

    @FXML
    public TextField messageBox;

    @FXML
    private Label onlineCountLabel;

    @FXML
    private HBox onlineUsersHbox;

    @FXML
    private ListView<String> userList;

    @FXML
    public Label usernameLabel;

    private Thread listenThread;

    @FXML
    void sendButtonAction() {
        if (!messageBox.getText().startsWith("/quit")) {
            App.client.println(messageBox.getText());
            messageBox.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messages.setText("");
        messageBox.setText("");
        usernameLabel.setText(App.client.getPseudo().get());
        onlineCountLabel.setText(String.valueOf(0));
        messages.setDisable(true);
        messages.setStyle("-fx-opacity: 1.0;");
        userList.setDisable(true);
        userList.setStyle("-fx-opacity: 1.0;");
        this.listenThread = new Thread(
                () -> {
                    try {
                        while (App.isShowing()) {
                            String line = App.client.readLine();
                            if (!line.startsWith("SERVER:")) {
                                if(line.endsWith("left the chat") || line.endsWith("joined the chat"))
                                {
                                    System.out.println("new client -> Send /list");
                                    App.client.println("/list");
                                }
                                messages.setText(messages.getText() + "\n" + line);
                            }else{
                                Platform.runLater(()->{
                                    var split = line.split(" ");
                                    var userOnline = split[1].split(",");
                                    onlineCountLabel.setText(String.valueOf(userOnline.length));
                                    userList.getItems().clear();//.forEach(userList.getItems()::remove);
                                    for (int i = 0; i< userOnline.length; i++) {
                                        userList.getItems().add(userOnline[i]);
                                    }
                                });

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        App.setOnCloseRequest((req)->{
            this.listenThread.interrupt();
        });
        this.listenThread.start();
    }

    @FXML
    void pressEnter(KeyEvent event) {
        var code = event.getCode();
        if (code.equals(KeyCode.ENTER)) {
            sendButtonAction();
        }
    }

    @FXML
    void previousView(MouseEvent event) throws UnprocessableViewException {
        App.client.println("/quit");
        this.listenThread.interrupt();
        App.setCurrentScene(new LauncherView());
    }

}

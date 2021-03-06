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

import gauncher.frontend.view.LauncherView;
import gauncher.frontend.view.SignInView;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class LoginController implements Initializable {

    private Logger log = new Logger("LoginController");

    @FXML
    private Pane errorMessage;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField userInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setVisible(false);
        userInput.setOnKeyPressed(key -> {
           if (key.getCode().equals(KeyCode.SPACE)) {
               this.errorMessage.setVisible(true);
               this.userInput.setText("");
               this.passwordInput.setText("");
           }
        });
        userInput.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.TAB)) {
                passwordInput.requestFocus();
            }
        });
    }

    @FXML
    public void signIn() throws UnprocessableViewException {
        App.setCurrentScene(new SignInView());
    }

    /**
     * Check connexion and login to Laucher
     * @throws UnprocessableViewException
     */
    @FXML
    public void login() throws UnprocessableViewException {

        var pseudoValue = this.userInput.getCharacters().toString();
        var passwordValue = this.passwordInput.getCharacters().toString();

        try {
            if (pseudoValue.isEmpty() || pseudoValue.isBlank()
                    || passwordValue.isEmpty() || passwordValue.isBlank()) {
                App.client.println("LOGIN");
                this.errorMessage.setVisible(true);
            } else {
                App.client.println(format("LOGIN %s %s", pseudoValue, passwordValue));
            }
            var response = App.client.readLine();
            if (response.startsWith("OK")) {
                App.client.setPseudo(pseudoValue);
                App.setCurrentScene(new LauncherView());
            } else {
                this.errorMessage.setVisible(true);
            }

        } catch (SocketException e) {
            log.error("Client have been disconnected from the server");
            App.setCurrentScene(new ConnectionView());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Login enter
     * @param event
     * @throws UnprocessableViewException
     */
    @FXML
    void inputValueUser(KeyEvent event) throws UnprocessableViewException {
        var code = event.getCode();
        if (code.equals(KeyCode.SPACE)) {
            System.out.println("LoginController.inputValue");
            this.errorMessage.setVisible(true);
            this.userInput.clear();
            this.passwordInput.clear();
        }
        if (code.equals(KeyCode.ENTER)) {
            this.login();
        }
        /*System.out.println("LoginController.inputValueUser");*/
    }

    @FXML
    void inputValuePass(KeyEvent event) throws UnprocessableViewException {
        this.inputValueUser(event);
    }

    private void setErrorMessage(String message) {
        this.errorMessage.setVisible(true);
    }
}
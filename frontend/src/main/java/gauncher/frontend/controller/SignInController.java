package gauncher.frontend.controller;

import static java.lang.String.format;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.view.ConnectionView;
import gauncher.frontend.view.LauncherView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    private Logger log = new Logger("SignInController");

    @FXML
    private Text confirmErrorPass;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Text messErrorPseudo;

    @FXML
    private PasswordField password;

    @FXML
    private TextField pseudo;

    @FXML
    void signIn(ActionEvent event) throws UnprocessableViewException {
        var pseudoValue = this.pseudo.getCharacters().toString();
        var passwordValue = this.password.getCharacters().toString();
        var confirmPasswordValue = this.confirmPassword.getCharacters().toString();

        try {
            if (pseudoValue.isBlank() || pseudoValue.isEmpty()
                    || passwordValue.isBlank() || passwordValue.isEmpty()
                    || confirmPasswordValue.isEmpty() || confirmPasswordValue.isBlank()
                    || !passwordValue.equals(confirmPasswordValue)) {
                if (pseudoValue.isEmpty()) {
                    this.messErrorPseudo.setVisible(true);
                }
                if(!passwordValue.equals(confirmPasswordValue)) {
                    this.confirmErrorPass.setVisible(true);
                }
            } else {
                App.client.println(format("SIGN %s %s", pseudoValue, passwordValue));
                var response = App.client.readLine();
                if (response.startsWith("OK")) {
                    App.client.setPseudo(pseudoValue);
                    App.setCurrentScene(new LauncherView());
                }
            }
        } catch (SocketException e) {
            log.error("Client have been disconnected from the server");
            App.setCurrentScene(new ConnectionView());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pseudo.setText("");
        password.setText("");
        confirmPassword.setText("");
        messErrorPseudo.setVisible(false);
        confirmErrorPass.setVisible(false);
        log.info("Sign In initialized");
    }
}

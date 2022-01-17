package gauncher.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML
    private Text confirmErrorPass;

    @FXML
    private Text messErrorPseudo;

    @FXML
    private TextField password;

    @FXML
    private TextField pseudo;

    @FXML
    void signIn(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

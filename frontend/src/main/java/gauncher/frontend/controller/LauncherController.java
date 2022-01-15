package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.view.ChatView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {

    Class aClass = this.getClass();

    @FXML
    private ImageView buttonBattleQuiz;

    @FXML
    private ImageView buttonChat;

    @FXML
    private ImageView buttonDemineur;

    @FXML
    private ImageView buttonJustePrix;

    @FXML
    private ImageView buttonLogout;

    @FXML
    private ImageView buttonMorpion;

    @FXML
    private ImageView buttonReversi;

    @FXML
    private ImageView logoGauncher;

    @FXML
    private HBox menu;

    @FXML
    private Text pseudoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(App.client.getPseudo().get());
        pseudoLabel.setText(App.client.getPseudo().get());
    }

    @FXML
    void logout(MouseEvent event) {
        App.client.println("quit");
        Stage stage = (Stage) buttonLogout.getScene().getWindow();
        stage.close();
    }

    @FXML
    void openChat(MouseEvent event) throws UnprocessableViewException {
        App.setCurrentScene(new ChatView());
    }
}

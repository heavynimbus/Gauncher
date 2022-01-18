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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {

    public final static String LOGO_TICTACTOE = "@/img/logoLogout.png.png";

    Class<?> aClass = this.getClass();

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
//        InputStream inputTicTacToe =  aClass.getResourceAsStream(LOGO_TICTACTOE);
//        assert inputTicTacToe != null;
//        Image imageTicTacToe = new Image(Objects.requireNonNull(getClass().getResource("img/logoLogout.png")).toExternalForm());
//        buttonMorpion.setVisible(true);
        var img = new Image(Objects.requireNonNull(getClass().getResource("/gauncher/frontend/controller/logoLogout.png")).toString(), true);
//        buttonMorpion.setImage(new Image("/home/llamorille/Documents/ISEN-Lille/Projets/Gauncher/frontend/src/main/resources/img/logoMorpion.png"));
//        var scene = buttonMorpion.getScene();
//        var img = new Image("/frontend/src/main/resources/img/logoMorpion.png");
//        var img = new Image(inputTicTacToe);
//        buttonMorpion.setVisible(true);
//        System.out.println("buttonMorpion = " + buttonMorpion.getImage());
//        System.out.println("scene = " + scene);0
        buttonMorpion.setImage(img);
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

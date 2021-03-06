package gauncher.frontend.controller;

import static java.lang.String.format;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.view.ChatView;
import gauncher.frontend.view.ConnectionView;
import gauncher.frontend.view.TictactoeView;
import gauncher.frontend.view.WorkInprogressView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {

    private Logger log = new Logger("LauncherController");

    @FXML
    private ImageView buttonLogout;

    @FXML
    private Text pseudoLabel;

    @FXML
    private Text chatCompt;

    @FXML
    private Text demineurCompt;

    @FXML
    private Text justePrixCompt;

    @FXML
    private Text quizCompt;

    @FXML
    private Text reversiCompt;

    @FXML
    private Text tictactoeCompt;


    /**
     * Initialization
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var init = "-/-";
        this.chatCompt.setText(init);
        this.tictactoeCompt.setText(init);
        this.justePrixCompt.setText(init);
        this.reversiCompt.setText(init);
        this.quizCompt.setText(init);
        this.demineurCompt.setText(init);

        try {
            this.setList();
        } catch (UnprocessableViewException e) {
            e.printStackTrace();
        }
        pseudoLabel.setText(App.client.getPseudo().get());
    }

    /**
     * Live and log out
     * @param event
     */
    @FXML
    void logout(MouseEvent event) {
        App.client.println("quit");
        Stage stage = (Stage) buttonLogout.getScene().getWindow();
        stage.close();
    }

    /**
     * If clic on chat Open it
     * @param event
     * @throws UnprocessableViewException
     */
    @FXML
    void openChat(MouseEvent event) throws UnprocessableViewException {
        App.client.println("PLAY chat");
        try {
            var response = App.client.readLine();
            if (response.startsWith("OK")) {
                log.info(format("%s start to play chat", App.client.getPseudo()));
                App.setCurrentScene(new ChatView());
            }
        } catch (SocketException e) {
            log.error("Client have been disconnected from the server");
            App.setCurrentScene(new ConnectionView());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If clic on tictactoe open it
     * @param event
     * @throws UnprocessableViewException
     */
    @FXML
    void openTicTacToe(MouseEvent event) throws UnprocessableViewException {
        App.client.println("PLAY tictactoe");
        try {
            var response = App.client.readLine();
            if (response.startsWith("OK")) {
                log.info(format("%s start to play tictactoe", App.client.getPseudo()));
                App.setCurrentScene(new TictactoeView());
            }
        } catch (SocketException e) {
            log.error("Client have been disconnected from the server");
            App.setCurrentScene(new ConnectionView());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        App.setCurrentScene(new TictactoeView());
    }

    /**
     *TODO : GAMES
     */



    @FXML
    void openReversi(MouseEvent event) throws UnprocessableViewException{
        App.setCurrentScene(new WorkInprogressView());
    }

    @FXML
    void openBattle(MouseEvent event) throws UnprocessableViewException {
        App.setCurrentScene(new WorkInprogressView());
    }

    @FXML
    void openDemineur(MouseEvent event) throws UnprocessableViewException {
        App.setCurrentScene(new WorkInprogressView());
    }

    @FXML
    void openJustePrix(MouseEvent event) throws UnprocessableViewException {
        App.setCurrentScene(new WorkInprogressView());
    }

    @FXML
    void reload() {
        try {
            this.setList();
        } catch (UnprocessableViewException e) {
            e.printStackTrace();
        }
    }

    private void setList() throws UnprocessableViewException {
        try {
            App.client.println("list");
            var res = App.client.readLine();
            if (res.startsWith("OK")) {
                var t = res.split(" ");
                this.parse(t[2].split(","));
            }
        } catch (SocketException e) {
            log.error("Client have been disconnected from the server");
            App.setCurrentScene(new ConnectionView());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parsing games list received from the server
     * @param list
     */
    private void parse(String[] list) {
        for (int i = 0; i < list.length; i++) {
            var index = list[i].indexOf("(");
            var temp = list[i].substring(index+1, list[i].length() - 1);
            var listPlayer = temp.split("/");
            if (list[i].startsWith("chat")) {
                this.chatCompt.setText(listPlayer[0]+"/"+ '\u221E');
            } else if (list[i].startsWith("tictactoe")) {
                this.tictactoeCompt.setText(listPlayer[0]+"/"+listPlayer[1]);
            } else if (list[i].startsWith("demineur")) {
                this.demineurCompt.setText(listPlayer[0]+"/"+listPlayer[1]);
            } else if (list[i].startsWith("battlequiz")) {
                this.quizCompt.setText(listPlayer[0]+"/"+listPlayer[1]);
            } else if (list[i].startsWith("reversi")) {
                this.reversiCompt.setText(listPlayer[0]+"/"+listPlayer[1]);
            } else if (list[i].startsWith("justePrix")) {
                this.justePrixCompt.setText(listPlayer[0]+"/"+listPlayer[1]);
            }
        }
    }
}
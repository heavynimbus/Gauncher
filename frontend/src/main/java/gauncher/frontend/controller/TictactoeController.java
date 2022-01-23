package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TictactoeController implements Initializable {

    private String x = "x";
    private String o = "o";
    private String player;

    public Logger log = new Logger("TicTacToe Controller");

    public String gameBoard;

    @FXML
    private Text case1;

    @FXML
    private Text case2;

    @FXML
    private Text case3;

    @FXML
    private Text case4;

    @FXML
    private Text case5;

    @FXML
    private Text case6;

    @FXML
    private Text case7;

    @FXML
    private Text case8;

    @FXML
    private Text case9;

    @FXML
    private GridPane grid;

    @FXML
    private Label playing;

    @FXML
    private Label seconde;

    private Thread listenThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initGame();
        setGameBoard("");
        System.out.println("test2");
        this.listenThread = new Thread(
                () -> {
                    try {
                        System.out.println("test");
                        while (App.isShowing()) {
                            System.out.println("ici");
                            String res = App.client.readLine();
                            System.out.println("res = " + res);
                            if (res.startsWith("READY")) {
                                if (res.contains("CROSS")) {
                                    this.player = x;
                                } else {
                                    this.player = o;
                                }
                                log.info(App.client.getPseudo().toString() + ": est pret");
                            } else {
                                this.grid.setDisable(!res.contains("PLAY"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        App.setOnCloseRequest((req) -> {
            this.listenThread.interrupt();
        });
        this.listenThread.start();
    }

    @FXML
    void confirm(ActionEvent event) {

    }

    @FXML
    void input(MouseEvent event) {
        var caseSelected = event.getTarget();
        if (testInput(caseSelected, "case1")) {
            this.case1.setText(player);
            this.playing.setText("Au joueur adverse de jouer");
        } else if (testInput(caseSelected, "case2")) {
            this.case2.setText(player);
        } else if (testInput(caseSelected, "case3")) {
            this.case3.setText(player);
        } else if (testInput(caseSelected, "case4")) {
            this.case4.setText(player);
        } else if (testInput(caseSelected, "case5")) {
            this.case5.setText(player);
        } else if (testInput(caseSelected, "case6")) {
            this.case6.setText(player);
        } else if (testInput(caseSelected, "case7")) {
            this.case7.setText(player);
        } else if (testInput(caseSelected, "case8")) {
            this.case8.setText(player);
        } else if (testInput(caseSelected, "case9")) {
            this.case9.setText(player);
        }
        setGameBoard(gameBoard);
    }

    private boolean testInput(EventTarget event, String caseTested) {
        return event.toString().contains(caseTested);
    }

    private void initGame() {
        this.player = x;
        this.case1.setText("");
        this.case2.setText("");
        this.case3.setText("");
        this.case4.setText("");
        this.case5.setText("");
        this.case6.setText("");
        this.case7.setText("");
        this.case8.setText("");
        this.case9.setText("");
        this.gameBoard = ".........";
    }

    private void setGameBoard(String board) {
        board = ".OX.XX.OO";
        var list = board.split("");
        for(int i=0; i<9; i++) {
            if (list[i].equals(".")) {
                list[i] = "";
            }
        }

        this.case1.setText(list[0]);
        this.case2.setText(list[1]);
        this.case3.setText(list[2]);
        this.case4.setText(list[3]);
        this.case5.setText(list[4]);
        this.case6.setText(list[5]);
        this.case7.setText(list[6]);
        this.case8.setText(list[7]);
        this.case9.setText(list[8]);
    }
}

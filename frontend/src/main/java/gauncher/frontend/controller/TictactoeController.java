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
    private Text playing;

    @FXML
    private Label seconde;

    private Thread listenThread;
    private int isPlaying;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initGame();
        System.out.println("Avant Thread");
        this.listenThread = new Thread(
                () -> {
                    try {
                        while (App.isShowing()) {
                            var list = this.gameBoard.split("");

                            for (int i = 0; i < this.gameBoard.length(); i++) {
                                this.grid.getChildren().get(i).setDisable(list[i].equals("O") || list[i].equals("X"));
                            }

                            String res = App.client.readLine();
                            System.out.println("res = " + res);
                            if (!res.startsWith("END")) {
                                this.playing.setText(" A l'adversaire");
                                if (res.startsWith("READY")) {
                                    if (res.contains("CROSS")) {
                                        this.player = x;
                                    } else {
                                        this.player = o;
                                    }
                                    System.out.println("player = " + this.player);
                                } else {
                                    if (res.contains("PLAY")) {
                                        this.playing.setText("A vous de jouer");
                                        setGameBoard(res);
                                        this.grid.setDisable(false);
                                    } else {
                                        this.grid.setDisable(true);
                                    }
                                }
                            } else {
                                this.grid.setDisable(true);
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

    private void test() {
        var list = this.gameBoard.split("");

        for (int i = 0; i < this.gameBoard.length(); i++) {
            this.grid.getChildren().get(i).setDisable(list[i].equals("O") || list[i].equals("X"));
        }
    }

    @FXML
    void confirm(ActionEvent event) {

    }

    @FXML
    void input(MouseEvent event) {
        System.out.println("TictactoeController.input");
        var caseSelected = event.getTarget();

        if (testInput(caseSelected, "case1")) {
            this.case1.setText(player);
            this.updateGameBoard(0);
        } else if (testInput(caseSelected, "case2")) {
            this.case2.setText(player);
            this.updateGameBoard(1);
        } else if (testInput(caseSelected, "case3")) {
            this.case3.setText(player);
            this.updateGameBoard(2);
        } else if (testInput(caseSelected, "case4")) {
            this.case4.setText(player);
            this.updateGameBoard(3);
        } else if (testInput(caseSelected, "case5")) {
            this.case5.setText(player);
            this.updateGameBoard(4);
        } else if (testInput(caseSelected, "case6")) {
            this.case6.setText(player);
            this.updateGameBoard(5);
        } else if (testInput(caseSelected, "case7")) {
            this.case7.setText(player);
            this.updateGameBoard(6);
        } else if (testInput(caseSelected, "case8")) {
            this.case8.setText(player);
            this.updateGameBoard(7);
        } else if (testInput(caseSelected, "case9")) {
            this.case9.setText(player);
            this.updateGameBoard(8);
        }
        this.grid.setDisable(true);
//        setGameBoard(gameBoard);
        System.out.println("OK "+gameBoard);
        App.client.println("OK " + gameBoard);
    }

    private void updateGameBoard(int index) {
        System.out.println("gameboard avant " + this.gameBoard);
        this.gameBoard = this.gameBoard.substring(0,index)+this.player.toUpperCase()+this.gameBoard.substring(index+1);
        System.out.println("Apres = " + this.gameBoard);
    }

    private boolean testInput(EventTarget event, String caseTested) {
        return event.toString().contains(caseTested);
    }

    private void initGame() {
        this.grid.setDisable(true);
        this.seconde.setVisible(false);
        this.player = x;
        this.playing.setText("");
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
        setGameBoard("A A .........");
    }

    private void setGameBoard(String board) {
        this.gameBoard = board.split(" ")[2];
        var list = this.gameBoard.split("");


        for(int i=0; i<9; i++) {
            if (list[i].equals(".")) {
                list[i] = "";
            }
        }

        this.case1.setText(list[0].toLowerCase());
        this.case2.setText(list[1].toLowerCase());
        this.case3.setText(list[2].toLowerCase());
        this.case4.setText(list[3].toLowerCase());
        this.case5.setText(list[4].toLowerCase());
        this.case6.setText(list[5].toLowerCase());
        this.case7.setText(list[6].toLowerCase());
        this.case8.setText(list[7].toLowerCase());
        this.case9.setText(list[8].toLowerCase());
    }
    
    private String parseBoard(String board) {
        System.out.println("TictactoeController.parseBoard");
        return board;
    }
}

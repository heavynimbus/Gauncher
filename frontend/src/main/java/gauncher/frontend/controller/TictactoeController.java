package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.logging.Logger;
import gauncher.frontend.view.LauncherView;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    @FXML
    private Text winning;


    private Thread listenThread;

    private int isPlaying;
    private boolean modeInput;

    private boolean firstInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initGame();
        this.listenThread = new Thread(
                () -> {
                    try {
                        while (App.isShowing()) {
                            var list = this.gameBoard.split("");
                            for (int i = 0; i < this.gameBoard.length(); i++) {
                                if(Arrays.stream(list).filter(this.player::equalsIgnoreCase).count() == 3) {
                                    System.out.println( i+ " = " + this.grid.getChildren().get(i).isDisable());
                                    this.grid.getChildren().get(i).setDisable(!list[i].equals(this.player.toUpperCase()));
                                    this.modeInput = true;
                                    System.out.println("this.modeInput = " + this.modeInput);
                                    System.out.println("this.firstInput = " + this.firstInput);
                                } else {
                                    this.grid.getChildren().get(i).setDisable(list[i].equals("O") || list[i].equals("X"));
                                }
                            }

                            String res = App.client.readLine();
                            System.out.println(res);
                            if (!res.startsWith("END")) {
                                this.playing.setText(" A l'adversaire");
                                if (res.startsWith("READY")) {
                                    if (res.contains("CROSS")) {
                                        this.player = x;
                                    } else {
                                        this.player = o;
                                    }
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
                                this.listenThread.interrupt();
                                setGameBoard(res);
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

    private void testInputValue(Text c,int idx){
        System.out.println("TictactoeController.testInputValue");
        System.out.println(idx);
        if (!modeInput) {
            c.setText(player);
            this.updateGameBoard(idx);
        }  else {
            if (!this.firstInput) {
                System.out.println("ici");
                System.out.println("1 " + this.gameBoard);
                this.updateGameBoard(idx, ".");
                System.out.println("4 " +this.gameBoard);
                setGameBoard("OK OK "+this.gameBoard);
                this.firstInput = true;
            } else {
                System.out.println("2 " +this.gameBoard);
                this.updateGameBoard(idx);
                System.out.println("3 "+this.gameBoard);
                this.modeInput = false;
                this.firstInput = false;
                setGameBoard("OK OK " + this.gameBoard);
            }
        }
    }

    @FXML
    void input(MouseEvent event) {
        var caseSelected = event.getTarget();
        if (testInput(caseSelected, "case1")) {
            testInputValue(this.case1,0);
        } else if (testInput(caseSelected, "case2")) {
            testInputValue(this.case2,1);
        } else if (testInput(caseSelected, "case3")) {
            testInputValue(this.case3,2);
        } else if (testInput(caseSelected, "case4")) {
            testInputValue(this.case4,3);
        } else if (testInput(caseSelected, "case5")) {
            testInputValue(this.case5,4);
        } else if (testInput(caseSelected, "case6")) {
            testInputValue(this.case6,5);
        } else if (testInput(caseSelected, "case7")) {
            testInputValue(this.case7,6);
        } else if (testInput(caseSelected, "case8")) {
            testInputValue(this.case8,7);
        } else if (testInput(caseSelected, "case9")) {
            testInputValue(this.case9,8);
        }
        if (!this.modeInput) {
            this.grid.setDisable(true);
            App.client.println("OK " + gameBoard);
        } else {
            for (int i = 0; i < this.gameBoard.length(); i++) {
                this.grid.getChildren().get(i).setDisable(!this.gameBoard.split("")[i].equals("."));
            }
        }

    }

    private void updateGameBoard(int index, String replace) {
        this.gameBoard = this.gameBoard.substring(0,index)+replace+this.gameBoard.substring(index+1);
    }

    private void updateGameBoard(int index) {
        updateGameBoard(index, this.player.toUpperCase());
    }

    private boolean testInput(EventTarget event, String caseTested) {
        return event.toString().contains(caseTested);
    }

    private void initGame() {
        this.grid.setDisable(true);
        this.seconde.setVisible(false);
        this.player = "";
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
        this.firstInput = false;
        this.modeInput = false;
        this.winning.setVisible(false);
        this.gameBoard = ".........";
        setGameBoard("A A .........");
    }

    private void setGameBoard(String board) {
        if (board.contains("END")) {
            this.gameBoard = board.split(" ")[3];
            this.winning.setText("Le gagnant est : " + board.split(" ")[1]);
            this.winning.setVisible(true);
        } else {
            this.gameBoard = board.split(" ")[2];
        }
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
}

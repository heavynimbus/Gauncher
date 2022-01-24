package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.logging.Logger;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TictactoeController implements Initializable {

    private final String x = "x";
    private final String o = "o";
    private String player;
    private Integer lastIdx;
    private Boolean testBlocked;

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
    private ArrayList<Integer> listPlayable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initGame();
        this.listenThread = new Thread(
                () -> {
                    try {
                        while (App.isShowing()) {
                            var list = this.gameBoard.split("");
                            for (int i = 0; i < this.gameBoard.length(); i++) {
                                if (Arrays.stream(list).filter(this.player::equalsIgnoreCase).count() == 3) {
                                    this.grid.getChildren().get(i).setDisable(!list[i].equals(this.player.toUpperCase()));
                                    this.modeInput = true;
                                } else {
                                    this.grid.getChildren().get(i).setDisable(list[i].equals("O") || list[i].equals("X"));
                                }
                            }

                            String res = App.client.readLine();
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

    private void testInputValue(Text c, int idx) {
        if (!modeInput) {
            c.setText(player);
            this.updateGameBoard(idx);
        } else {
            if (!this.firstInput) {
                if (!this.testBlocked) {
                    this.updateGameBoard(idx, ".");
                    setGameBoard("OK OK " + this.gameBoard);
                    lastIdx = idx;
                    setPlayable(lastIdx);
                    this.firstInput = true;
                } else {
                    this.firstInput = false;
                }
            } else {
                if (checkIfCanPlay(idx)) {
                    this.updateGameBoard(idx);
                    setGameBoard("OK OK " + this.gameBoard);
                    this.modeInput = false;
                    this.firstInput = false;
                }
            }
        }
    }

    /**
     * @param next
     * @return
     */
    private boolean checkIfCanPlay(Integer next) {
        return this.listPlayable.contains(next);
    }

    /**
     * Fonction appelé lors d'un clic sur une case
     *
     * @param event
     */
    @FXML
    void input(MouseEvent event) {
        var caseSelected = event.getTarget();
        if (testInput(caseSelected, "case1")) {
            testInputValue(this.case1, 0);
        } else if (testInput(caseSelected, "case2")) {
            testInputValue(this.case2, 1);
        } else if (testInput(caseSelected, "case3")) {
            testInputValue(this.case3, 2);
        } else if (testInput(caseSelected, "case4")) {
            testInputValue(this.case4, 3);
        } else if (testInput(caseSelected, "case5")) {
            testInputValue(this.case5, 4);
        } else if (testInput(caseSelected, "case6")) {
            testInputValue(this.case6, 5);
        } else if (testInput(caseSelected, "case7")) {
            testInputValue(this.case7, 6);
        } else if (testInput(caseSelected, "case8")) {
            testInputValue(this.case8, 7);
        } else if (testInput(caseSelected, "case9")) {
            testInputValue(this.case9, 8);
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

    /**
     * Update le board
     *
     * @param index
     * @param replace
     */
    private void updateGameBoard(int index, String replace) {
        this.gameBoard = this.gameBoard.substring(0, index) + replace + this.gameBoard.substring(index + 1);
    }

    private void updateGameBoard(int index) {
        updateGameBoard(index, this.player.toUpperCase());
    }

    /**
     * Permet de savoir ou l'utilisateur clique
     *
     * @param event
     * @param caseTested
     * @return
     */
    private boolean testInput(EventTarget event, String caseTested) {
        return event.toString().contains(caseTested);
    }

    /**
     * Initialisation
     */
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
        this.listPlayable = new ArrayList<>();
        this.testBlocked = false;
        this.gameBoard = ".........";
        setGameBoard("A A .........");
    }

    /**
     * Print le board
     *
     * @param board
     */
    private void setGameBoard(String board) {
        if (board.contains("END")) {
            this.gameBoard = board.split(" ")[3];
            this.winning.setText("Le gagnant est : " + board.split(" ")[1]);
            this.winning.setVisible(true);
        } else {
            this.gameBoard = board.split(" ")[2];
        }
        var list = this.gameBoard.split("");

        for (int i = 0; i < 9; i++) {
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

        this.listPlayable = new ArrayList<>();
    }

    /**
     * Permet de controller si l'utilisateur à le droit de déplacer sa piece sur le 2e clic
     *
     * @param index
     */
    private void setPlayable(Integer index) {
        for (int i = 0; i < 9; i++) {
            this.grid.getChildren().get(i).setDisable(true);
        }

        Map<Integer, List<Integer>> cellRelationShips = Map.of(
                0, List.of(1, 3, 4),
                1, List.of(-1, +1, +2, +3, +4),
                2, List.of(-1, 2, 3),
                3, List.of(-3, -2, 1, 3, 4),
                4, List.of(-4, -3, -2, -1, 1, 2, 3, 4),
                5, List.of(-1, -4, -3, 2, 3),
                6, List.of(-3, -2, 1),
                7, List.of(-4, -3, -2, -1, 1),
                8, List.of(-1, -3, -4)
        );
        var splitedBoard = gameBoard.split("");
        cellRelationShips.keySet().stream()
                .filter(index::equals)
                .forEach(key -> {
                    var neighbours = cellRelationShips.get(key);
                    neighbours.forEach((neighbourIndex) -> {
                        if ( !splitedBoard[index + neighbourIndex].equalsIgnoreCase(o) &&
                                !splitedBoard[index + neighbourIndex].equalsIgnoreCase(x)) {
                            this.grid.getChildren().get(index + neighbourIndex).setDisable(false);
                            this.listPlayable.add(index + neighbourIndex);
                        } else {
                            this.grid.getChildren().get(index + neighbourIndex).setDisable(true);
                        }
                    });
                });

        boolean test = this.grid.getChildren().stream().allMatch(Node::isDisable);
        this.grid.getChildren().stream().map(elt -> elt.getId() + " " + elt.isDisable()).forEach(System.out::println);
        System.out.println("grid " + this.grid.isDisable());
        if (test) {
            System.out.println("test = " + test);
            updateGameBoard(lastIdx, this.player);
        }
    }
}

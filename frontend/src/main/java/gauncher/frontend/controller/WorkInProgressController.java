package gauncher.frontend.controller;

import gauncher.frontend.App;
import gauncher.frontend.exception.UnprocessableViewException;
import gauncher.frontend.view.LauncherView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkInProgressController implements Initializable {


    @FXML
    void previousView(MouseEvent event) throws UnprocessableViewException {
        App.setCurrentScene(new LauncherView());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

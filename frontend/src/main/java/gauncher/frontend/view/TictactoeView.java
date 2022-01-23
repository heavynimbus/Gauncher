package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class TictactoeView extends View{
    public TictactoeView() throws UnprocessableViewException {
        super("/fxml/tictactoe.fxml");
    }
}

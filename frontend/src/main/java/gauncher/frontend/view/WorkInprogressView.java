package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class WorkInprogressView extends View{
    public WorkInprogressView() throws UnprocessableViewException {
        super("/fxml/workInProgress.fxml");
    }
}

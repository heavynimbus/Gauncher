package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class LauncherView extends View{

    public LauncherView() throws UnprocessableViewException {
        super("/fxml/launcher_view.fxml");
    }
}

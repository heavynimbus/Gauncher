package gauncher.frontend.view;

import gauncher.frontend.exception.UnprocessableViewException;

public class SignInView extends View {
    public SignInView() throws UnprocessableViewException {
        super("/fxml/signIn.fxml");
    }
}
